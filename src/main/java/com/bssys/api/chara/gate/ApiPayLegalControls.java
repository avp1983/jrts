package com.bssys.api.chara.gate;

import com.bssys.api.types.TransferLegalSend;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;
import com.bssys.entity.UMTWebProvider;
import com.bssys.entity.UMTWebProvider_;
import com.bssys.session.UserSessionBean;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS_LEGAL_ENTITIES;
import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;

@Stateless
@Named
public class ApiPayLegalControls {

  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private UmtAgentFacade agentFacade;

  @Inject
  private UserSessionBean userSession;

  public void checkPointCreditPilotAllowed(int pointId) throws ApiException {
    if (!agentFacade.getAgentAllowActions(pointId).contains(CAN_SEND_TRANSFERS_LEGAL_ENTITIES)) {
      throw new ApiException(ApiExceptionType.API_ERR_ACCESS_DENIED, userSession.getLocRes("creditpilotapigate", "MSG_API_ERR_ACCESS_DENIED_TRANSFER"));
    }

    Query query = em.createNativeQuery(
        "select cust.AllowedTransferSysSender " +
            "from UMTAgentLinks links " +
            " inner join Customer cust on links.ParentLink = cust.CustId " +
            "where ChildLink = ? " +
            "order by PathLength").setParameter(1, pointId);

    List<String> allows = (List<String>) query.getResultList();
    if (allows.isEmpty()) {
      throw new ApiException(ApiExceptionType.API_ERR_ACCESS_DENIED, userSession.getLocRes("creditpilotapigate", "MSG_API_ERR_ACCESS_DENIED_SYSTEM"));
    }

    String allowedList;
    if (!allows.get(0).equalsIgnoreCase("inherited")) {
      allowedList = allows.get(0);
    } else {
      allowedList = allows.get(allows.size() - 1);
    }

    String[] sysList = allowedList.split(",");
    if (!Arrays.asList(sysList).contains(String.valueOf(TRANSFER_SYS_CREDIT_PILOT))) {
      throw new ApiException(ApiExceptionType.API_ERR_ACCESS_DENIED, userSession.getLocRes("creditpilotapigate", "MSG_API_ERR_ACCESS_DENIED_SYSTEM"));
    }
  }

  public void checkPointOwnerOfPayLegal(int pointId, UmtPayLegal payLegal) throws ApiException {
    if (payLegal.getDocPK().getClient() != pointId) {
      throw new ApiException(ApiExceptionType.API_ERR_TRANSFER_CREATED_BY_OTHER_CLIENT, userSession.getLocRes("creditpilotapigate", "MSG_API_ERR_TRANSFER_CREATED_BY_OTHER_CLIENT"));
    }
  }

  private List<UMTWebProvider> getUmtWebProvidersById(int providerId) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UMTWebProvider> query = cb.createQuery(UMTWebProvider.class);
    Root<UMTWebProvider> root = query.from(UMTWebProvider.class);
    query.where(cb.equal(root.get(UMTWebProvider_.ID), providerId));
    query.select(root);
    TypedQuery<UMTWebProvider> webProviderTypedQuery = em.createQuery(query);
    return webProviderTypedQuery.getResultList();
  }

  public void checkProviderById(int providerId) throws ApiException {
    List<UMTWebProvider> providerList = getUmtWebProvidersById(providerId);

    if (providerList.isEmpty()) {
      throw new ApiException(ApiExceptionType.API_ERR_PROVIDER_NOT_FOUND, String.valueOf(providerId));
    }

    if (!providerList.get(0).isActive()) {
      throw new ApiException(ApiExceptionType.API_ERR_PROVIDER_NOT_ACTIVE, String.valueOf(providerId));
    }
  }

  private boolean moneyEquals(BigDecimal a, Float b) {
    return (a.setScale(2, BigDecimal.ROUND_HALF_UP).compareTo(BigDecimal.valueOf(b).setScale(2, BigDecimal.ROUND_HALF_UP)) == 0);
  }

  /**
   * Проверка что пользователь пытается изменить некоторые атрибуты уже созданного сообщения
   *
   * @param request
   * @param payLegal
   * @throws ApiException
   */
  public void checkStaticAttributesChanged(TransferLegalSend request, UmtPayLegal payLegal) throws ApiException {
    if (((int) request.getProviderId()) != payLegal.getProvider().getID()) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CANNOT_CHANGE_PARAMS, "Идентификатор провайдера");
    }

    if (!moneyEquals(request.getAmount(), payLegal.getAmount())) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CANNOT_CHANGE_PARAMS, "Сумма перевода");
    }

    if (!request.getCurrencyAlpha().equalsIgnoreCase(payLegal.getCurrCodeIso())) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CANNOT_CHANGE_PARAMS, "Валюта перевода");
    }

    if ((request.getFee() != null)
        && (!moneyEquals(request.getFee(), payLegal.getCommission()))) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CANNOT_CHANGE_PARAMS, "Комиссия");
    }
  }

  /**
   * Проверяем что пользователь:
   * не передал параметры которых нет в базе
   * передал все требуемые параметры
   * не пытается изменить параметры которые недоступны для изменения (editable = false)
   * или были отправлены на прошлом шагу.
   *
   * @param exFields параметры которые передал пользователь
   * @param payLegal перевод, с чьими параметрами мы должны сверить параметры от пользователя
   */
  public void checkParametersChanged(UmtPayLegalExFields dbFields, UmtPayLegalExFields exFields, UmtPayLegal payLegal) throws ApiException {
    checkUnnecessaryParameters(dbFields, exFields);
    checkInsufficientParameters(dbFields, exFields);
    checkProcessedParameters(payLegal, dbFields, exFields);
    checkReadOnlyParameters(dbFields, exFields);
  }

  /**
   * Проверка что не изменены параметры которые помечены только для чтения
   *
   * @param dbFields параметры хранящиеся в базе данных
   * @param exFields параметры которые передал пользователь
   * @throws ApiException
   */
  private void checkReadOnlyParameters(UmtPayLegalExFields dbFields, UmtPayLegalExFields exFields) throws ApiException {
    List<String> notIdenticalParamNames = new ArrayList<>();
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      boolean parameterValueMatch = true;
      for (UmtPayLegalExField dbField : dbFields.getRecList()) {
        if (dbField.getExName().equalsIgnoreCase(exField.getExName())) {
          if (dbField.getIsEditable() == 0) {
            if (!dbField.getExValue().equals(exField.getExValue())) {
              parameterValueMatch = false;
            }
          }
        }
      }
      if (!parameterValueMatch) {
        notIdenticalParamNames.add(exField.getExName());
      }
    }
    if (notIdenticalParamNames.size() > 0) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CHANGED_READ_ONLY_PARAMS, StringUtils.join(notIdenticalParamNames, ", "));
    }
  }

  /**
   * Проверка что не изменились параметры отправленные на прошлых шагах
   *
   * @param payLegal перевод
   * @param dbFields параметры хранящиеся в базе данных
   * @param exFields параметры которые передал пользователь
   */
  private void checkProcessedParameters(UmtPayLegal payLegal, UmtPayLegalExFields dbFields, UmtPayLegalExFields exFields) throws ApiException {
    if (payLegal.getCurrentStep() == 0) {
      return;
    }
    List<String> notIdenticalParamNames = new ArrayList<>();
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      boolean parameterValueMatch = true;
      for (UmtPayLegalExField dbField : dbFields.getRecList()) {
        if (dbField.getExName().equalsIgnoreCase(exField.getExName())) {
          if (dbField.getStep() < payLegal.getCurrentStep()) {
            if (!dbField.getExValue().equals(exField.getExValue())) {
              parameterValueMatch = false;
            }
          }
        }
      }
      if (!parameterValueMatch) {
        notIdenticalParamNames.add(exField.getExName());
      }
    }
    if (notIdenticalParamNames.size() > 0) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_CHANGED_PROCESSED_PARAMS, StringUtils.join(notIdenticalParamNames, ", "));
    }
  }

  private void checkInsufficientParameters(UmtPayLegalExFields dbFields, UmtPayLegalExFields exFields) throws ApiException {
    List<String> insufficientParamNames = new ArrayList<>();
    for (UmtPayLegalExField dbField : dbFields.getRecList()) {
      boolean parameterExists = false;
      if (dbField.getIsEditable() == 1) {
        for (UmtPayLegalExField exField : exFields.getRecList()) {
          if (dbField.getExName().equalsIgnoreCase(exField.getExName())) {
            parameterExists = true;
          }
        }
        if (!parameterExists) {
          insufficientParamNames.add(dbField.getExName());
        }
      }
    }

    if (insufficientParamNames.size() > 0) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_INSUFFICIENT_PARAMS, StringUtils.join(insufficientParamNames, ", "));
    }
  }

  private void checkUnnecessaryParameters(UmtPayLegalExFields dbFields, UmtPayLegalExFields exFields) throws ApiException {
    List<String> unnecessaryParamNames = new ArrayList<>();
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      boolean parameterExists = false;
      for (UmtPayLegalExField dbField : dbFields.getRecList()) {
        if (exField.getExName().equalsIgnoreCase(dbField.getExName())) {
          parameterExists = true;
        }
      }
      if (!parameterExists) {
        unnecessaryParamNames.add(exField.getExName());
      }
    }
    if (unnecessaryParamNames.size() > 0) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_UNNECESSARY_PARAMS, StringUtils.join(unnecessaryParamNames, ", "));
    }
  }

  public void checkCurrCode(TransferLegalSend request) throws ApiException {
    if (request.getCurrencyAlpha() == null) {
      return;
    }

    if (!request.getCurrencyAlpha().equalsIgnoreCase("RUR")) {
      throw new ApiException(ApiExceptionType.API_ERR_PAY_LEGAL_BAD_CURR_CODE, request.getCurrencyAlpha());
    }
  }
}
