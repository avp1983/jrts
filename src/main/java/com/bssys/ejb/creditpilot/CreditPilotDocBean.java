package com.bssys.ejb.creditpilot;

import com.bssys.ejb.AdminNotificationBean;
import com.bssys.ejb.creditpilot.api.ResponseResolver;
import com.bssys.ejb.creditpilot.api.checkpay.CheckPayRequest;
import com.bssys.ejb.creditpilot.api.checkpay.FindCheckResult;
import com.bssys.ejb.creditpilot.api.findpay.FindPayDocState;
import com.bssys.ejb.creditpilot.api.findpay.FindPayRequest;
import com.bssys.ejb.creditpilot.api.findpay.FindPayResponse;
import com.bssys.ejb.creditpilot.api.paydoc.PayDocRequest;
import com.bssys.ejb.creditpilot.api.preparedoc.PrepareDocRequest;
import com.bssys.ejb.creditpilot.api.types.CheckPayDataType;
import com.bssys.ejb.creditpilot.api.types.KpDealer;
import com.bssys.ejb.creditpilot.api.types.PaymentType;
import com.bssys.ejb.creditpilot.api.types.ResultType;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFieldParams;
import com.bssys.lang.Date.DatesUtils;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultType;
import com.bssys.session.UserSessionBean;
import com.google.common.collect.ImmutableMap;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXB;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bssys.ejb.creditpilot.ActionType.*;
import static com.bssys.ejb.creditpilot.CreditPilotFacade.PLEASE_CONTACT_CP_ADMIN;
import static com.bssys.ejb.creditpilot.api.checkpay.FindCheckResultCode.FIND_CHECK_OK;
import static com.bssys.ejb.creditpilot.api.checkpay.FindCheckResultCode.FIND_CHECK_WAIT_RESPONSE;
import static com.bssys.ejb.creditpilot.api.findpay.FindPayDocState.FIND_PAY_OK;
import static com.bssys.ejb.creditpilot.api.findpay.FindPayResultCode.FIND_PAY_RESULT_FAILURE;
import static com.bssys.ejb.creditpilot.api.findpay.FindPayResultCode.FIND_PAY_RESULT_SUCCESS;
import static com.bssys.ejb.creditpilot.api.paydoc.PayDocResultCode.PAY_DOC_RES_CODE_OK;
import static com.bssys.ejb.creditpilot.api.preparedoc.PrepareDocResultCode.PREPARE_DOC_RES_CODE_OK;
import static com.bssys.ejb.creditpilot.api.types.CheckPayDataType.CheckPayMap.CheckPayMapEntry;
import static com.bssys.ejb.creditpilot.api.types.CheckPayDataType.CheckPayTable.CheckPayTableRow;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_ERROR;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;

@Stateless
@Named
public class CreditPilotDocBean implements Serializable {

  private static final int FIND_CHECK_DELAY = 2 * MILLIS_PER_SECOND;
  private static final int FIND_CHECK_TRY_COUNT = 30;
  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private CreditPilotFacade creditPilotFacade;
  @Inject
  private UserSessionBean userSession;
  @Inject
  private CreditPilotSettingsFacade settingsFacade;
  @Inject
  private ResponseResolver responseResolver;
  @Inject
  private AdminNotificationBean adminNotificationBean;


  private KpDealer readKpDealer(Response response) {
    String checkPayResStr = response.readEntity(String.class);
    KpDealer checkPayResult;
    try (StringReader reader = new StringReader(checkPayResStr.replaceAll("billnumber", "billNumber"))) {
      checkPayResult = JAXB.unmarshal(reader, KpDealer.class);
    }
    return checkPayResult;
  }


  public FindCheckResult checkPay(CheckPayRequest checkPayRequest) {
    String billNumber;
    Response response;
    try {
      response = creditPilotFacade.getResponse(CHECKPAY, checkPayRequest.toRequestParamsMap());
    } catch (CreditPilotCheckResponseException e) {
      return new FindCheckResult(responseResolver.getResultForCommonError());
    }
    try {
      KpDealer checkPayResult = readKpDealer(response);
      billNumber = String.valueOf(checkPayResult.getBillNumber());

    } finally {
      response.close();
    }

    int tryCount = 0;
    KpDealer findCheckResponse;
    KpDealer.Result findCheckResult;
    do {
      try {
        Thread.sleep(FIND_CHECK_DELAY);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      tryCount++;

      Response findCheckHttpResponse;
      try {
        findCheckHttpResponse = creditPilotFacade.getResponse(FINDCHECK, ImmutableMap.of("billNumber", billNumber));
      } catch (CreditPilotCheckResponseException e) {
        return new FindCheckResult(responseResolver.getResultForCommonError());
      }
      try {
        findCheckResponse = findCheckHttpResponse.readEntity(KpDealer.class);
      } finally {
        findCheckHttpResponse.close();
      }
      findCheckResult = findCheckResponse.getResult();
    } while ((tryCount < FIND_CHECK_TRY_COUNT) &&
        (findCheckResult == null ||
            Objects.equals(findCheckResult.getResultCode().intValue(), FIND_CHECK_WAIT_RESPONSE))
        && findCheckResponse.getPayment().size() == 0);


    if (findCheckResult != null && (findCheckResult.getResultCode().intValue() != FIND_CHECK_OK)) {
      return responseResolver.getErrorStepResultForFindCheck(findCheckResult);
    }

    CheckPayDataType payData = findCheckResponse.getPayment().get(0).getCheckPayData();

    Map<String, String> servicePayParams = new HashMap<>();
    for (CheckPayMapEntry serviceParam : payData.getCheckPayMap().getCheckPayMapEntry()) {
      servicePayParams.put(serviceParam.getName(), serviceParam.getValue());
    }

    List<UmtPayLegalExField> additionalPayParams = new ArrayList<>();
    int stepNum = checkPayRequest.getStepsCount() == null ?
        1 : checkPayRequest.getStepsCount() - payData.getStepsLeft().intValue() + 1;
    for (CheckPayTableRow addPayParam : payData.getCheckPayTable().getCheckPayTableRow()) {
      UmtPayLegalExField payLegalExField = new UmtPayLegalExField();
      payLegalExField.setExName(addPayParam.getBillingName());
      payLegalExField.setExTitle(addPayParam.getTitle());
      payLegalExField.setIsEditable(addPayParam.isEditable() ? 1 : 0);
      payLegalExField.setStep(stepNum);
      payLegalExField.setDescription(addPayParam.getTitle());
      payLegalExField.setExValue(addPayParam.getValue());

      if (addPayParam.getCheckPayParam().size() > 0) {
        payLegalExField.setChoiceValuesXML(new UmtPayLegalExFieldParams(addPayParam.getCheckPayParam()));
      }
      additionalPayParams.add(payLegalExField);
    }

    return new FindCheckResult(ProcessStepResult.EMPTY_OK, payData.getStepsLeft().intValue(), servicePayParams,
        additionalPayParams);
  }

  public ProcessStepResult prepareDoc(PrepareDocRequest prepareDocRequest) {
    Response response;
    try {
      response = creditPilotFacade.getResponse(PREPARE, prepareDocRequest.toRequestParamsMap());
    } catch (CreditPilotCheckResponseException e) {
      return responseResolver.getResultForCommonError();
    }
    KpDealer result;
    try {
      result = response.readEntity(KpDealer.class);
    } finally {
      response.close();
    }

    ProcessStepResultType stepResultLevel =
        (result.getResult().getResultCode().intValue() == PREPARE_DOC_RES_CODE_OK) ?
            PROCESS_RESULT_OK : PROCESS_RESULT_ERROR;
    return new ProcessStepResult(result.getResult().getResultDescription(), stepResultLevel);
  }

  public ProcessStepResult payDoc(PayDocRequest payDocRequest) {
    Map<String, String> requestParams = payDocRequest.toRequestParamsMap();
    requestParams.put("terminalId", String.valueOf(userSession.getAgentId()));
    requestParams.put("remotesystemId", settingsFacade.getApplicationId());
    KpDealer result;
    Response response;
    try {
      response = creditPilotFacade.getResponse(PAY, requestParams);
    } catch (CreditPilotCheckResponseException e) {
      return responseResolver.getResultForCommonError();
    }
    try {
      result = response.readEntity(KpDealer.class);
    } finally {
      response.close();
    }
    ProcessStepResultType stepResultLevel =
        (result.getResult().getResultCode().intValue()
            == PAY_DOC_RES_CODE_OK) ? PROCESS_RESULT_OK : PROCESS_RESULT_ERROR;
    return new ProcessStepResult(result.getResult().getResultDescription(), stepResultLevel);
  }

  public FindPayResponse findPayDoc(FindPayRequest findPayRequest) {
    KpDealer result;
    Response response;
    try {
      response = creditPilotFacade.getResponse(FINDPAY, findPayRequest.toRequestParamsMap());
    } catch (CreditPilotCheckResponseException e) {
      return new FindPayResponse(responseResolver.getResultForCommonError());
    }
    try {
      result = response.readEntity(KpDealer.class);
    } finally {
      response.close();
    }
    if (result.getPayment().size() == 0) {
      return new FindPayResponse(responseResolver.getResultForCommonError(), null, false);
    }
    PaymentType payment = result.getPayment().get(0);
    FindPayDocState payResultCode = FindPayDocState.byCode(payment.getResult().getResultCode().intValue());
    return new FindPayResponse(responseResolver.getResultForFindPay(payResultCode), payment,
        payment.getResult().isFatal());
  }

  public void checkNotFinishedDocs() {
    TypedQuery<UmtPayLegal> allNotFinishedDocs = em.createNamedQuery("UMTPayLegal.notFinishedDocs", UmtPayLegal.class);
    for (UmtPayLegal doc : allNotFinishedDocs.getResultList()) {
      FindPayResponse stepFindPayResult = findPayDoc(new FindPayRequest(doc.getCheckNumber()));

      if (stepFindPayResult.getFoundPayment() == null) {
        adminNotificationBean.sendEmailToAdmin(new ProcessStepResult(
            String.format("По переводу %d, отправленному %s в систему КредитПилот, не получено сообщение о " +
                    "проведении перевода у провайдера. %s", doc.getID(),
                DatesUtils.formatDMY(doc.getValueDateTime()), PLEASE_CONTACT_CP_ADMIN
            ),
            PROCESS_RESULT_ERROR, "Система КредитПилот не вернула данные по переводу"
        ));
        doc.setIsRemoteOperationComplete(FIND_PAY_RESULT_FAILURE);
        em.merge(doc);
        continue;
      }

      ResultType result = stepFindPayResult.getFoundPayment().getResult();
      FindPayDocState payResultCode = FindPayDocState.byCode(result.getResultCode().intValue());
      if (result.isFatal()) {
        if (payResultCode != FIND_PAY_OK) {
          adminNotificationBean.sendEmailToAdmin(new ProcessStepResult(
              String.format("При проведении перевода %s, отправленного %s в систему КредитПилот, " +
                      "произошла ошибка: код ошибки  %d, описание «%s», описание ошибки провайдера «%s». %s",
                  doc.getID(), DatesUtils.formatDMY(doc.getValueDateTime()), result.getResultCode().intValue(),
                  result.getResultDescription(), result.getProviderResultMessage(), PLEASE_CONTACT_CP_ADMIN
              ),
              PROCESS_RESULT_ERROR, "Система КредитПилот не вернула данные по переводу"
          ));
          doc.setIsRemoteOperationComplete(FIND_PAY_RESULT_FAILURE);
        } else {
          doc.setIsRemoteOperationComplete(FIND_PAY_RESULT_SUCCESS);
        }
        em.merge(doc);
      }
    }
  }

}
