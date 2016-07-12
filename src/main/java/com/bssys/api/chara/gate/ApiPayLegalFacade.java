package com.bssys.api.chara.gate;

import com.bssys.api.types.Document;
import com.bssys.api.types.TransferLegalArchiveRequest;
import com.bssys.api.types.TransferLegalSend;
import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.ejb.creditpilot.api.checkpay.FindCheckResult;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;
import com.bssys.entity.PayLegal.UmtPayLegal_;
import com.bssys.entity.UMTWebProvider_;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultType;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.Statuses;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.Objects;

import static com.bssys.ejb.UmtPayLegalFacade.PROVIDER_PAYMENT_TYPE_MULTI_STEP;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;

@Stateless
@Named
public class ApiPayLegalFacade {

  @Inject
  private UmtPayLegalFacade payLegalFacade;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private ApiPayLegalControls payLegalControls;

  @PersistenceContext
  private transient EntityManager em;

  public ApiPayLegalFacade() {
    payLegalFacade = new UmtPayLegalFacade();
  }

  private void markParamsStepZero(UmtPayLegalExFields exFields) {
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      exField.setStep(0);
    }
  }

  /**
   * Создаем новый перевод в адрес ЮЛ
   *
   * @param send     данные перевода из запроса клиента
   * @param exFields параметры которые прислал клиент
   * @return новый перевод
   */
  private UmtPayLegal createNewPayLegal(TransferLegalSend send, UmtPayLegalExFields exFields) throws ApiException {
    payLegalControls.checkProviderById((int) send.getProviderId());

    UmtPayLegal payLegal = payLegalFacade.createNewDocumentToProvider((int) send.getProviderId());
    UmtPayLegalExFields dbFields = payLegalFacade.createExFieldsByProviderParams(payLegal.getProvider().getParams());
    payLegalControls.checkParametersChanged(dbFields, exFields, payLegal);

    payLegalFacade.lazyInitNewDocument(payLegal, exFields);
    markParamsStepZero(exFields);
    return payLegal;
  }

  /**
   * Загружаем сохраненные в базе параметры для перевода в адрес ЮЛ
   *
   * @param payLegal перевод из базы
   * @param exFields поля присланные клиентом
   */
  private void loadStoredExFields(UmtPayLegal payLegal, UmtPayLegalExFields exFields) {
    UmtPayLegalExFields storedExFields = payLegalFacade.getExFields(payLegal);
    for (UmtPayLegalExField clientExField : exFields.getRecList()) {
      for (UmtPayLegalExField storedExField : storedExFields.getRecList()) {
        if (storedExField.getExName().equalsIgnoreCase(clientExField.getExName())) {
          if (storedExField.getIsEditable() == 1) {
            storedExField.setExValue(clientExField.getExValue());
          }
        }
      }
    }
    exFields.setRecList(storedExFields.getRecList());
  }

  private UmtPayLegal getPayLegalByCheckNumber(String checkNumber) throws ApiException {
    UmtPayLegal payLegal = payLegalFacade.getDocumentByChecknumber(checkNumber);
    if (payLegal == null) {
      throw new ApiException(ApiExceptionType.API_ERR_TCN_NOT_FOUND, checkNumber);
    }
    return payLegal;
  }

  /**
   * Поиск существующего перевода в базе по КНП
   *
   * @param send     данные перевода из запроса клиента
   * @param exFields параметры перевода от клиента
   * @return загрженный из базы перевод
   */
  private UmtPayLegal loadPayLegalByCheckNumber(TransferLegalSend send, UmtPayLegalExFields exFields) throws ApiException {
    UmtPayLegal payLegal = getPayLegalByCheckNumber(send.getChecknumber());
    payLegalControls.checkStaticAttributesChanged(send, payLegal);

    UmtPayLegalExFields dbFields = payLegalFacade.getExFields(payLegal);
    payLegalControls.checkParametersChanged(dbFields, exFields, payLegal);

    loadStoredExFields(payLegal, exFields);
    return payLegal;
  }

  /**
   * Поиск перевода для подтверждения
   *
   * @param request
   * @param exFields
   * @return
   * @throws ApiException
   */
  public UmtPayLegal getPayLegal(TransferLegalSend request, UmtPayLegalExFields exFields) throws ApiException {
    return loadPayLegalByCheckNumber(request, exFields);
  }

  /**
   * Поиск перевода для удаления
   *
   * @param legalDelete
   * @return
   */
  public UmtPayLegal getPayLegal(Document.TransferLegal.DeleteRequest legalDelete) throws ApiException {
    return getPayLegalByCheckNumber(legalDelete.getChecknumber());
  }

  /**
   * Создание или поиск существующего перевода в адрес ЮЛ
   *
   * @param request полный запрос от клиента
   * @return перевод в адрес ЮЛ
   */
  public UmtPayLegal createPayLegal(TransferLegalSend request, UmtPayLegalExFields exFields) throws ApiException {
    if ((request.getChecknumber() == null) || (request.getChecknumber().isEmpty())) {
      return createNewPayLegal(request, exFields);
    } else {
      return loadPayLegalByCheckNumber(request, exFields);
    }
  }

  /**
   * Сохранение перевода в адрес ЮЛ
   *
   * @param payLegal отправка перевода и подтверждение в кредит пилот
   */
  public void savePayLegal(UmtPayLegal payLegal, UmtPayLegalExFields exFields) throws ApiException {
    List<ProcessStepResult> saveResult = payLegalFacade.saveDocumentLocal(payLegal, exFields, true);
    if (ProcessStepResult.hasError(saveResult) || ProcessStepResult.hasWarning(saveResult)) {
      throw ApiException.createApiMessage(saveResult);
    }

    if (Objects.equals(payLegal.getProvider().getPaymentType(), PROVIDER_PAYMENT_TYPE_MULTI_STEP)
        && !Objects.equals(payLegal.getStepsLeft(), 0)) {
      FindCheckResult findCheckResult = payLegalFacade.makeMultiStepActionInExternalSystem(payLegal, exFields);
      if (findCheckResult.getResult().getType() != PROCESS_RESULT_OK) {
        throw ApiException.createApiMessage(findCheckResult.getResult());
      }

      for (UmtPayLegalExField addExField : findCheckResult.getAdditionalPayRequisites()) {
        exFields.getRecList().add(addExField);
      }
      payLegalFacade.setExFields(payLegal, exFields);

      if (!Objects.equals(payLegal.getStepsLeft(), 0) ||
          payLegalFacade.hasEditableFields(exFields, payLegal.getCurrentStep())) {
        return;
      }
    }

    ProcessStepResult prepareStepResult = payLegalFacade.prepareDocInExternalSystem(payLegal, exFields);
    if (prepareStepResult.getType() != ProcessStepResultType.PROCESS_RESULT_OK) {
      throw ApiException.createApiMessage(prepareStepResult);
    }
  }

  /**
   * Подтверждаем перевод в адрес ЮЛ
   *
   * @param payLegal перевод
   */
  public void confirmPayLegal(UmtPayLegal payLegal) throws ApiException {
    List<ProcessStepResult> saveResult = payLegalFacade.confirmDocument(payLegal, true);
    if (ProcessStepResult.hasError(saveResult) || ProcessStepResult.hasWarning(saveResult)) {
      throw ApiException.createApiMessage(saveResult);
    }

    payLegalFacade.changeCurrentLimit(payLegal);
    if (ProcessStepResult.hasError(saveResult) || ProcessStepResult.hasWarning(saveResult)) {
      throw ApiException.createApiMessage(saveResult);
    }
  }

  public void deletePayLegal(UmtPayLegal payLegal) throws ApiException {
    ProcessStepResult result = payLegalFacade.deleteDoc(payLegal);
    if (result.getType() != PROCESS_RESULT_OK) {
      throw new ApiException(ApiExceptionType.API_ERR_DOC_NOT_PASSED_CONTROLS, result.getDescription());
    }
  }

  /**
   * Список переводов из архива
   *
   * @param archiveRequest
   * @return
   */
  public List<UmtPayLegal> getArchivePayLegal(TransferLegalArchiveRequest archiveRequest) {
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UmtPayLegal> query = cb.createQuery(UmtPayLegal.class);
    Root<UmtPayLegal> root = query.from(UmtPayLegal.class);
    Predicate p = cb.conjunction();
    p = cb.and(p, cb.equal(root.get(UmtPayLegal_.pointId), userSession.getAgentId()));
    p = cb.and(p, cb.equal(root.get("status"), Statuses.STS_PAYLEGAL_PAID));
    if (archiveRequest.getChecknumber() != null) {
      p = cb.and(p, cb.equal(root.get(UmtPayLegal_.checkNumber), archiveRequest.getChecknumber()));
    }
    if (archiveRequest.getProviderId() != null) {
      p = cb.and(p, cb.equal(root.get(UmtPayLegal_.provider).get(UMTWebProvider_.ID), archiveRequest.getProviderId()));
    }
    if (archiveRequest.getLastname() != null) {
      p = cb.and(p, cb.like(root.get(UmtPayLegal_.senderLastName), archiveRequest.getLastname()));
    }
    if (archiveRequest.getFirstname() != null) {
      p = cb.and(p, cb.like(root.get(UmtPayLegal_.senderFirstName), archiveRequest.getFirstname()));
    }
    if (archiveRequest.getSendDateFrom() != null) {
      p = cb.and(p, cb.greaterThanOrEqualTo(root.get(UmtPayLegal_.valueDateTime), archiveRequest.getSendDateFrom().toGregorianCalendar().getTime()));
    }
    if (archiveRequest.getSendDateTo() != null) {
      p = cb.and(p, cb.lessThanOrEqualTo(root.get(UmtPayLegal_.valueDateTime), archiveRequest.getSendDateTo().toGregorianCalendar().getTime()));
    }
    query.where(p).orderBy(cb.asc(root.get(UmtPayLegal_.valueDateTime)));
    query.select(root);

    TypedQuery<UmtPayLegal> payLegalTypedQuery = em.createQuery(query);
    return payLegalTypedQuery.getResultList();
  }
}
