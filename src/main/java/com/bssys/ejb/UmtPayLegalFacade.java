package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.bls.types.DboRootDocumentFacade;
import com.bssys.ejb.creditpilot.CreditPilotDocBean;
import com.bssys.ejb.creditpilot.api.checkpay.CheckPayRequest;
import com.bssys.ejb.creditpilot.api.checkpay.FindCheckResult;
import com.bssys.ejb.creditpilot.api.findpay.FindPayRequest;
import com.bssys.ejb.creditpilot.api.findpay.FindPayResponse;
import com.bssys.api.chara.gate.ApiException;
import com.bssys.api.chara.gate.ApiPayLegalControls;
import com.bssys.ejb.creditpilot.api.paydoc.PayDocRequest;
import com.bssys.ejb.creditpilot.api.preparedoc.PrepareDocRequest;
import com.bssys.entity.*;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalCommission;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFieldParam;
import com.bssys.entity.PayLegal.UmtPayLegalExFieldParams;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.rts.RtsConnector;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.Statuses;
import com.bssys.umt.TransferSys;
import com.google.common.base.Strings;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.xml.bind.JAXB;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.bssys.ejb.creditpilot.api.findpay.FindPayResultCode.FIND_PAY_RESULT_SUCCESS;
import static com.bssys.ejb.creditpilot.api.findpay.FindPayResultCode.FIND_PAY_RESULT_WAIT;
import static com.bssys.process.step.ProcessStepResult.EMPTY_OK;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_ERROR;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_WARNING;
import static com.bssys.umt.Statuses.*;
import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;
import static com.bssys.umt.TransferSys.TRANSFER_SYS_NATIVE;

@Named("UmtPayLegalFacade")
@Stateless
public class UmtPayLegalFacade implements Serializable {

  public static final int PROVIDER_PAYMENT_TYPE_MULTI_STEP = 16010;
  private static final int MIN_YEAR = 1900;
  private static final Date MIN_DATE = new DateTime().withDate(MIN_YEAR, 1, 1).toDate();

  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private CreditPilotDocBean creditPilotFacade;

  @Inject
  private DboRootDocumentFacade rootDocumentFacade;

  @Inject
  private UMTWebProviderFacade providerFacade;

  @Inject
  private RtsConnector rtsConnector;
  @Inject
  private UmtAgentFacade agentFacade;

  @Inject
  private ApiPayLegalControls apiPayLegalControls;

  @Inject
  private UMTDictSettingsFacade dictSettingsFacade;

  public UmtPayLegal createNewDocument() {
    UmtPayLegal doc = new UmtPayLegal();
    rootDocumentFacade.prepareDocumentForSend(doc, userSession.getAgentId());
    em.persist(doc);
    em.flush();

    return doc;
  }

  public String getCheckNumber(Integer docID) {
    return rtsConnector.executeWithGet("RT_2UMTPayLegal", "GetTCNForPayLegal", "ID", String.valueOf(docID));
  }

  public UmtPayLegalExFields createExFieldsByProviderParams(List<UMTWebProviderParam> params) {
    UmtPayLegalExFields res = new UmtPayLegalExFields();
    res.setRecList(new ArrayList<UmtPayLegalExField>());

    for (UMTWebProviderParam param : params) {
      res.getRecList().add(getExFieldByProviderParam(param));
    }
    return res;
  }

  public void lazyInitNewDocument(UmtPayLegal doc, UmtPayLegalExFields exFields) {
    em.merge(doc);
    rootDocumentFacade.setDocStatus(doc, STS_BSI_FOO, "Создание документа");
    doc.setCheckNumber(getCheckNumber(doc.getID()));
    setExFields(doc, exFields);
    em.merge(doc);
    em.flush();
  }

  public UmtPayLegal createNewDocumentToProvider(int providerID) {
    UmtPayLegal doc = createNewDocument();
    UMTWebProvider provider = em.find(UMTWebProvider.class, providerID);
    List<UMTWebProvCategories> categoriesList = providerFacade.getProviderCategories(provider);
    doc.setProvider(provider);
    if (!categoriesList.isEmpty()) {
      doc.setProviderCategories(categoriesList.get(0).getName());
    }
    doc.setReceiverTransferSys(TRANSFER_SYS_CREDIT_PILOT);
    doc.setSenderTransferSys(TRANSFER_SYS_NATIVE);
    doc.setUserSender(userSession.getUserKey());
    doc.setDocumentNumber(doc.getID().toString());
    doc.setCurrCodeIso("RUR");
    doc.setTransferIssueCountryCode(provider.getCountries().get(0).getCountryCode());
    doc.setDemoPay(agentFacade.isDemoAgent(userSession.getAgentId()) ? 1 : 0);

    doc.setPointId(doc.getCustID());
    return doc;
  }

  public void setExFields(UmtPayLegal doc, UmtPayLegalExFields blobFields) {
    byte[] blobsString = rtsConnector.prepareBlobFieldValue(rootDocumentFacade.getTableName(doc),
        doc.getDocPK().toDelimString(), "ExFields", blobFields.getRecList());
    doc.setExFields(blobsString);
  }

  public UmtPayLegalExFields getExFields(UmtPayLegal doc) {
    doc.getExFields();
    return rootDocumentFacade.getDocumentBlobFields(doc, "ExFields", UmtPayLegalExFields.class);
  }

  private List<ProcessStepResult> checkDocumentRTS(UmtPayLegal doc) {
    List<ProcessStepResult> result = new ArrayList<>();
    String docCheckRes =
        rtsConnector.executeWithGet("RT_2UMTPayLegal", "CheckDocumentByID", "ID", String.valueOf(doc.getID()));

    if (Strings.isNullOrEmpty(docCheckRes)) {
      result.add(new ProcessStepResult("Система временно недоступна", PROCESS_RESULT_ERROR));
      return result;
    }

    RTSControlResult rtsControlResult;
    try (StringReader reader = new StringReader(docCheckRes)) {
      rtsControlResult = JAXB.unmarshal(reader, RTSControlResult.class);
    }

    if (!Strings.isNullOrEmpty(rtsControlResult.getErrors().trim())) {
      result.add(new ProcessStepResult(rtsControlResult.getErrors(), PROCESS_RESULT_ERROR));
    }
    if (!Strings.isNullOrEmpty(rtsControlResult.getWarnings().trim())) {
      result.add(new ProcessStepResult(rtsControlResult.getWarnings(), PROCESS_RESULT_WARNING));
    }
    return result;
  }

  private float zeroIfNull(Float val) {
    return val == null ? 0 : val;
  }

  private ProcessStepResult checkMinDate(Date dateFields, final String fieldLabel) {
    if (dateFields != null && MIN_DATE.after(dateFields)) {
      return new ProcessStepResult(
          "Некорректное значение поля \"" + fieldLabel + "\": дата должна быть позже 1900 года", PROCESS_RESULT_ERROR);
    }
    return EMPTY_OK;
  }

  private ProcessStepResult checkBeforeCurrentDate(Date dateFields, final String fieldLabel) {
    if (dateFields != null && !(new Date()).after(dateFields)) {
      return new ProcessStepResult(
          "Некорректное значение поля \"" + fieldLabel + "\": дата должна быть раньше текущей", PROCESS_RESULT_ERROR);
    }
    return EMPTY_OK;
  }

  private List<ProcessStepResult> checkDocument(UmtPayLegal doc) {
    List<ProcessStepResult> result = new ArrayList<>();
    result.add(checkMinDate(doc.getSenderBirthday(), "Дата рождения"));
    result.add(checkBeforeCurrentDate(doc.getSenderBirthday(), "Дата рождения"));
    result.add(checkMinDate(doc.getSenderLicenceDateIssue(), "Документ, удостоверяющий личность: Дата выдачи"));
    result.add(checkBeforeCurrentDate(doc.getSenderLicenceDateIssue(), "Документ, удостоверяющий личность: Дата выдачи"));
    result.add(checkMinDate(doc.getSenderOtherDocDateStart(), "Миграционная карта или иной документ : Действует с"));
    result.add(checkBeforeCurrentDate(doc.getSenderOtherDocDateStart(), "Миграционная карта или иной документ : Действует с"));
    result.add(checkMinDate(doc.getSenderOtherDocDateFinish(), "Миграционная карта или иной документ : Действует по"));
    return result;
  }

  public List<ProcessStepResult> saveDocumentLocal(UmtPayLegal doc, UmtPayLegalExFields additionalParams, boolean forceSave) {
    List<ProcessStepResult> result = new ArrayList<>();

    try {
      apiPayLegalControls.checkPointCreditPilotAllowed(userSession.getAgentId());
    } catch (ApiException e) {
      result.add(new ProcessStepResult(e.getMessage(), PROCESS_RESULT_ERROR));
      return result;
    }

    if (isDocSigned(doc)) {
      result.add(new ProcessStepResult("Неверный статус документа для сохранения", PROCESS_RESULT_ERROR));
      return result;
    }
    result.addAll(checkDocument(doc));
    if (ProcessStepResult.hasError(result)) {
      return result;
    }

    setExFields(doc, additionalParams);
    em.merge(doc);
    em.flush();

    ProcessStepResult updateComRes = updateCommission(doc);
    if (updateComRes.getType() != PROCESS_RESULT_OK) {
      result.add(updateComRes);
      return result;
    }
    em.merge(doc);
    em.flush();

    if (doc.getStepsLeft() == null || doc.getStepsLeft() == 0) {
      List<ProcessStepResult> checkRTSResult = checkDocumentRTS(doc);
      if (ProcessStepResult.hasError(checkRTSResult)) {
        result.addAll(checkRTSResult);
        return result;
      } else if (ProcessStepResult.hasWarning(checkRTSResult)) {
        if (!forceSave) {
          result.addAll(checkRTSResult);
          return result;
        } else {
          result.clear();
        }
      }
    }

    if (doc.getStatus() == STS_BSI_FOO) {
      rootDocumentFacade.setDocStatus(doc, STS_NEW, "Первичное сохранение");
    }

    saveSenderToClients(doc);
    em.merge(doc);
    em.flush();

    result.add(EMPTY_OK);
    return result;
  }

  private void saveSenderToClients(UmtPayLegal doc) {
    if (dictSettingsFacade.senderFieldsFilled(doc)) {
      UmtTransferClient senderClient = dictSettingsFacade.findClientByPayLegalSender(doc);
      if (senderClient != null){
        fillClientBySender(senderClient,doc);
        em.merge(senderClient);
        em.flush();
      }
      else {
        senderClient = new UmtTransferClient();
        fillClientBySender(senderClient,doc);
        em.persist(senderClient);
        em.flush();
      }
      doc.setSenderId(senderClient.getId());
      doc.setSenderCardNumber(senderClient.getCardNumber());
    }
  }

  private ProcessStepResult updateCommission(UmtPayLegal doc) {
    try (StringReader reader = new StringReader(
        rtsConnector.executeWithGet("RT_2UMTPayLegal", "CalcPayLegalCommissionByDocID",
            "ID", String.valueOf(doc.getID()))
    )) {
      UmtPayLegalCommission payLegalCommission = JAXB.unmarshal(reader, UmtPayLegalCommission.class);
      if (!Strings.isNullOrEmpty(payLegalCommission.getErrMsg())) {
        return new ProcessStepResult("Ошибка при расчете комиссии за перевод: " + payLegalCommission.getErrMsg(),
            PROCESS_RESULT_ERROR);
      }
      doc.setCommissionId(payLegalCommission.getComID());
      doc.setCommission(payLegalCommission.getCommission());
      doc.setProviderCommission(payLegalCommission.getProviderCommission());
      doc.setAddCommission(payLegalCommission.getAddCommission());
      doc.setAgentCommission(payLegalCommission.getAgentCommission());
      doc.setSysCommission(payLegalCommission.getSysCommission());
    }
    return EMPTY_OK;
  }

  public Map<String, String> getCurrentStepAdditionalParams(int currentStep, UmtPayLegalExFields additionalParams) {
    HashMap<String, String> res = new HashMap<>();
    for (UmtPayLegalExField exField : additionalParams.getRecList()) {
      if (StringUtils.equalsIgnoreCase(exField.getExName(), "phoneNumber") ||
          (exField.getStep() >= currentStep && (exField.getIsEditable() == 1))) {
        res.put(exField.getExName(), exField.getExValue());
      }
    }
    return res;
  }

  public FindCheckResult makeMultiStepActionInExternalSystem(UmtPayLegal doc, UmtPayLegalExFields additionalParams) {
    if (doc.getStatus() != STS_NEW && doc.getStatus() != STS_BSI_FOO) {
      return new FindCheckResult(
          new ProcessStepResult("Неверный статус документа для проверки во внешней системе", PROCESS_RESULT_ERROR));
    }

    FindCheckResult stepCheckPay = creditPilotFacade.checkPay(
        new CheckPayRequest(doc.getProvider().getPaymentType(), doc.getCheckNumber(), doc.getProvider().getID(),
            zeroIfNull(doc.getAmount()), getCurrentStepAdditionalParams(doc.getCurrentStep(), additionalParams),
            doc.getStepsCount())
    );

    if (stepCheckPay.getResult().getType() == PROCESS_RESULT_ERROR) {
      return stepCheckPay;
    }

    if (doc.getStepsCount() == null) {
      doc.setStepsCount(stepCheckPay.getStepsLeft() + 1);
    }
    for (Map.Entry<String, String> serviceField : stepCheckPay.getServicePayParams().entrySet()) {
      String serviceFieldName = serviceField.getKey().toUpperCase();
      if (serviceFieldName.equals("COMMISSION")) {
        doc.setProviderCommission(Float.parseFloat(serviceField.getValue()));
        em.merge(doc);
        em.flush();

        ProcessStepResult updateComRes = updateCommission(doc);
        if (updateComRes.getType() != PROCESS_RESULT_OK) {
          return new FindCheckResult(updateComRes);
        }
      }
    }
    doc.setStepsLeft(stepCheckPay.getStepsLeft());
    rootDocumentFacade.setDocStatus(doc, STS_NEW, "Промежуточное сохранение при многошаговом режиме");
    em.merge(doc);
    return stepCheckPay;
  }

  public ProcessStepResult prepareDocInExternalSystem(UmtPayLegal doc, UmtPayLegalExFields additionalParams) {
    if (doc.getStatus() != STS_NEW) {
      return new ProcessStepResult("Неверный статус документа для проверки во внешней системе", PROCESS_RESULT_ERROR);
    }

    ProcessStepResult stepPrepareResult = creditPilotFacade.prepareDoc(
        new PrepareDocRequest(doc.getCheckNumber(), doc.getProvider().getID(), zeroIfNull(doc.getAmount()),
            zeroIfNull(doc.getAmount()) + zeroIfNull(doc.getProviderCommission()),
            getPayRequestMap(additionalParams)
        )
    );
    if (stepPrepareResult == null || stepPrepareResult.getType() == PROCESS_RESULT_ERROR) {
      return stepPrepareResult;
    }

    rootDocumentFacade.setDocStatus(doc, STS_PAYLEGAL_READY_TOPAY, "Корректно пройдена проверка во внешней системе");
    em.merge(doc);

    return new ProcessStepResult("Документ успешно сохранён", PROCESS_RESULT_OK);
  }

  public ProcessStepResult deleteDoc(UmtPayLegal doc) {
    if (!isAvailableDelete(doc)) {
      return new ProcessStepResult("Неверный статус документа для сохранения", PROCESS_RESULT_ERROR);
    }

    if (doc.getStatus() == STS_BSI_FOO) {
      UmtPayLegal docForDel = findDoc(doc.getDocPK());
      em.remove(docForDel);
    } else {
      rootDocumentFacade.setDocStatus(doc, STS_DELETED, "Удаление документа по требование оператора");
      em.merge(doc);
    }
    return EMPTY_OK;
  }

  private Map<String, String> getPayRequestMap(UmtPayLegalExFields exFields) {
    Map<String, String> result = new HashMap<>();
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      if (Objects.equals(exField.getIsEditable(), 1)) {
        result.put(exField.getExName(), exField.getExValue());
      }
    }
    return result;
  }

  public List<ProcessStepResult> confirmDocument(UmtPayLegal doc, boolean forceSave) {
    List<ProcessStepResult> result = new ArrayList<>();

    if (!isAvailableConfirm(doc)) {
      result.add(new ProcessStepResult("Неверный статус документа для сохранения", PROCESS_RESULT_ERROR));
      return result;
    }

    /* if (!isDocSigned(doc)) {
      result.add(new ProcessStepResult("Документ не подписан", PROCESS_RESULT_ERROR));
      return result;
    } */

    List<ProcessStepResult> checkRTSResult = checkDocumentRTS(doc);
    if (ProcessStepResult.hasError(checkRTSResult)) {
      result.addAll(checkRTSResult);
      return result;
    } else if (ProcessStepResult.hasWarning(checkRTSResult)) {
      if (!forceSave) {
        result.addAll(checkRTSResult);
        return result;
      } else {
        result.clear();
      }
    }

    ProcessStepResult stepPayResult = creditPilotFacade.payDoc(
        new PayDocRequest(doc.getCheckNumber(), doc.getProvider().getID(), zeroIfNull(doc.getAmount()),
            zeroIfNull(doc.getAmount()) + zeroIfNull(doc.getCommission()), getPayRequestMap(getExFields(doc)))
    );
    if (stepPayResult.getType() != PROCESS_RESULT_OK) {
      result.add(stepPayResult);
      return result;
    }

    FindPayResponse stepFindPayResult = creditPilotFacade.findPayDoc(new FindPayRequest(doc.getCheckNumber()));
    result.add(stepFindPayResult.getResult());
    if (stepFindPayResult.getResult().getType() == PROCESS_RESULT_ERROR) {
      return result;
    }

    rootDocumentFacade.setDocStatus(doc, STS_CLIENT_ACCEPTED, "Документ успешно принят внешней системой");
    doc.setValueDateTime(new Date());
    doc.setIsRemoteOperationComplete(stepFindPayResult.isRemoteOperationComplete() ? FIND_PAY_RESULT_SUCCESS : FIND_PAY_RESULT_WAIT);
    em.merge(doc);

    result.add(EMPTY_OK);
    return result;
  }

  public ProcessStepResult changeCurrentLimit(UmtPayLegal doc) {
    ProcessStepResult stepChangeLimit = makeChangeCurrentLimit(doc.getID());
    if (stepChangeLimit.getType() == PROCESS_RESULT_ERROR) {
      return stepChangeLimit;
    }

    rootDocumentFacade.setDocStatus(doc, STS_PAYLEGAL_PAID, "Лимит списан, остаток уменьшен");
    em.merge(doc);
    em.flush();
    return EMPTY_OK;
  }

  private ProcessStepResult makeChangeCurrentLimit(Integer docId) {
    ExecResult execResult;
    try (StringReader reader = new StringReader(
        rtsConnector.executeWithGet("RT_2UMTPayLegal", "ChangeCurrentLimitPayLegalByDocID",
            "ID", String.valueOf(docId))
    )) {
      execResult = JAXB.unmarshal(reader, ExecResult.class);
    }

    if (execResult.getStatus() == 1) {
      return EMPTY_OK;
    } else {
      return new ProcessStepResult(execResult.getErrMsg(), PROCESS_RESULT_ERROR);
    }
  }

  public boolean isAvailableSave(UmtPayLegal doc) {
    return doc.getStatus() == Statuses.STS_BSI_FOO || doc.getStatus() == Statuses.STS_NEW;
  }

  public boolean isAvailableConfirm(UmtPayLegal doc) {
    return doc.getStatus() == Statuses.STS_PAYLEGAL_READY_TOPAY;
  }

  public boolean isAvailableDelete(UmtPayLegal doc) {
    return doc.getStatus() == Statuses.STS_BSI_FOO || doc.getStatus() == Statuses.STS_NEW
        || doc.getStatus() == Statuses.STS_PAYLEGAL_READY_TOPAY;
  }

  public boolean isDocSigned(UmtPayLegal doc) {
    return (rootDocumentFacade.isDocSigned(doc));
  }

  public UmtPayLegal findDoc(DboDocPK docPK) {
    return em.find(UmtPayLegal.class, docPK);
  }

  public String getAmountWithCurrency(DboDocPK docPK) {
    UmtPayLegal doc = em.find(UmtPayLegal.class, docPK);
    if (doc == null) {
      return null;
    }
    return doc.getAmountWithCurrency();
  }

  public UmtPayLegal getDocumentByID(DboDocPK pk) {
    return em.find(UmtPayLegal.class, pk);
  }

  public DboDocPK copy(DboDocPK pk) {
    try {
      UmtPayLegal doc = em.find(UmtPayLegal.class, pk);
      UmtPayLegalExFields oldExFields = getExFields(doc);
      doc.setStepsLeft(null);
      doc.setStepsCount(null);
      doc.setDocumentNumber(null);
      doc.setCommission(null);
      doc.setProviderCommission(null);
      doc.setIsRemoteOperationComplete(null);
      em.detach(doc);
      rootDocumentFacade.prepareDocumentForSend(doc, userSession.getAgentId());
      doc.setCheckNumber(null);
      doc.clearID();
      em.persist(doc);
      doc.setDocumentNumber(doc.getID().toString());
      em.flush();
      lazyInitNewDocument(doc, getExFieldsForCopy(oldExFields));
      em.merge(doc);
      return doc.getDocPK();
    } catch (Exception e) {
      return null;
    }
  }

  private UmtPayLegalExFields getExFieldsForCopy(UmtPayLegalExFields oldExFields) {
    UmtPayLegalExFields resExFields = new UmtPayLegalExFields();
    for (UmtPayLegalExField exField : oldExFields.getRecList()) {
      if (exField.getStep() == 0) {
        resExFields.getRecList().add(exField);
      }
    }
    return resExFields;
  }

  public boolean hasEditableFields(UmtPayLegalExFields exFields, int currentStep) {
    for (UmtPayLegalExField exField : exFields.getRecList()) {
      if (exField.getStep() == currentStep && exField.getIsEditable() == 1) {
        return true;
      }
    }
    return false;
  }

  @XmlRootElement(name = "Result")
  @XmlAccessorType(XmlAccessType.FIELD)
  private static class ExecResult {
    @SuppressWarnings("UnusedDeclaration")
    @XmlAttribute(name = "status")
    private Integer status;
    @SuppressWarnings("UnusedDeclaration")
    private String errMsg;

    public Integer getStatus() {
      return status;
    }

    public String getErrMsg() {
      return errMsg;
    }
  }

  public UmtPayLegalExField getExFieldByProviderParam(UMTWebProviderParam param) {
    UmtPayLegalExField res = new UmtPayLegalExField();
    res.setExName(param.getName());
    res.setExTitle(Strings.isNullOrEmpty(param.getTitle()) ? param.getPatternDesc() : param.getTitle());
    res.setIsEditable(1);
    res.setStep(0);
    res.setDescription(param.getPatternDesc());
    if (param.getItems() != null && param.getItems().size() != 0) {
      UmtPayLegalExFieldParams exFieldParams = new UmtPayLegalExFieldParams();
      for (UMTWebProvParamElem paramElem : param.getItems()) {
        exFieldParams.getPayParams().add(new UmtPayLegalExFieldParam(paramElem.getName(), paramElem.getValue()));
      }
      res.setChoiceValuesXML(exFieldParams);
    }
    return res;
  }

  public UmtPayLegal getDocumentByChecknumber(String checknumber) {
    TypedQuery<UmtPayLegal> query = em.createNamedQuery("UMTPayLegal.getPayLegalByChecknumber", UmtPayLegal.class)
        .setParameter("checkNumber", checknumber);
    List<UmtPayLegal> items = query.getResultList();
    if (items.isEmpty()) {
      return null;
    } else {
      return items.get(0);
    }
  }

  private void fillClientBySender(UmtTransferClient client, UmtPayLegal payLegal){
    client.setAccount(payLegal.getSenderAccount());
    client.setBirthday(payLegal.getSenderBirthday());
    client.setBirthPlace(payLegal.getSenderBirthPlace());
    client.setCity(payLegal.getSenderCity());
    client.setCountryISO(payLegal.getSenderCountryISO());
    client.setFirstName(payLegal.getSenderFirstName());
    client.setFlat(payLegal.getSenderFlat());
    client.setHouse(payLegal.getSenderHouse());
    client.setiNN(payLegal.getSenderINN());
    client.setIsResident(payLegal.getSenderIsResident());
    client.setLastName(payLegal.getSenderLastName());
    client.setLicenceDateIssue(payLegal.getSenderLicenceDateIssue());
    client.setLicenceDepCode(payLegal.getSenderLicenceDepCode());
    client.setLicenceIssuer(payLegal.getSenderLicenceIssuer());
    client.setLicenceNumber(payLegal.getSenderLicenceNumber());
    client.setLicenceSeries(payLegal.getSenderLicenceSeries());
    client.setLicenceType(payLegal.getSenderLicenceType());
    client.setMigrationCardNumber(payLegal.getSenderMigrationCardNumber());
    client.setNationalityCode(payLegal.getSenderNationalityCode());
    client.setPatronymic(payLegal.getSenderPatronymic());
    client.setPhone(payLegal.getSenderPhone());
    client.setEmail(payLegal.getSenderEmail());
    client.setStreet(payLegal.getSenderStreet());
    client.setHouseBlock(payLegal.getSenderHouseBlock());
    client.setHouseBuilding(payLegal.getSenderHouseBuilding());
    client.setClient(payLegal.getDocPK().getClient());
    client.setCustId(payLegal.getCustID());
    client.setSystemId(TransferSys.TRANSFER_SYS_NATIVE);
  }

}
