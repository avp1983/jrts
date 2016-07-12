package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;
import com.bssys.ejb.CountryFacade;
import com.bssys.ejb.TransferClientFacade;
import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.ejb.creditpilot.api.checkpay.FindCheckResult;
import com.bssys.entity.Country;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.entity.PayLegal.UmtPayLegalExFieldParam;
import com.bssys.entity.PayLegal.UmtPayLegalExFields;
import com.bssys.entity.UMTWebProviderCountry;
import com.bssys.entity.UMTWebProviderParam;
import com.bssys.entity.UmtTransferClient;
import com.bssys.lang.Date.DatesUtils;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultController;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.ApplicationSettingsBean;
import com.google.common.base.Joiner;
import com.google.common.base.Strings;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.inputmask.InputMask;
import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.selectonemenu.SelectOneMenu;
import org.primefaces.component.tooltip.Tooltip;
import org.primefaces.context.RequestContext;
import org.primefaces.event.SelectEvent;

import javax.el.ValueExpression;
import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.component.UISelectItem;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlInputText;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.bssys.conversion.Currency.RUR;
import static com.bssys.ejb.UmtPayLegalFacade.PROVIDER_PAYMENT_TYPE_MULTI_STEP;
import static com.bssys.process.step.ProcessStepResult.EMPTY_OK;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_ERROR;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_WARNING;

@Named("payLegalController")
@ViewScoped
public class PayLegalController implements Serializable {
  private static final int MAX_COUNTRY_LIST_SIZE = 5;
  private static final int MAX_CLIENT_SIZE = 5;
  private static final int RTS_RESPONSE_RES_CODE_ERROR = 7;


  @Inject
  private UmtPayLegalFacade payLegalFacade;
  @Inject
  private CountryFacade countryFacade;
  @Inject
  private TransferClientFacade transferClientFacade;
  @Inject
  private UserSessionBean userSession;
  @Inject
  private ProcessStepResultController resultController;
  @Inject
  private UmtPayTransferOpenFacade transferFacade;
  @Inject
  private UmtAgentFacade agentFacade;
  @Inject
  private UMTWebProviderFacade providerFacade;
  @Inject
  private ApplicationSettingsBean applicationSettingsBean;

  private UmtPayLegal currentDoc;

  private Map<UmtPayLegalExField, String> paramComponentBinding = new HashMap<>();

  private UmtPayLegalExFields exFields = new UmtPayLegalExFields();

  private boolean isAdditionalParamsInited;
  private boolean isUseSenderAccount = false;
  private boolean isView;
  private boolean forceSave;

  public void initCurrentDoc() {
    if (currentDoc != null) {
      return;
    }
    Map<String, String> inParams = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    String inDocIDR = inParams.get("IDR");
    isView = "View".equals(inParams.get("action"));

    if (inDocIDR != null) {
      currentDoc = payLegalFacade.findDoc(new DboDocPK(inDocIDR));
      exFields = payLegalFacade.getExFields(currentDoc);
      return;
    }

    int providerID = NumberUtils.toInt(inParams.get("providerid"), 0);
    if (providerID == 0) {
      throw new RuntimeException("Не переданы параметры для формирования документа Перевод в пользу юр.лиц");
    }

    currentDoc = payLegalFacade.createNewDocumentToProvider(providerID);
    exFields = payLegalFacade.createExFieldsByProviderParams(currentDoc.getProvider().getParams());
    RequestContext.getCurrentInstance().execute("lazyInitNewDocument()");
  }

  public void lazyInitNewDocument() {
    if (!payLegalFacade.isAvailableSave(currentDoc)) {
      return;
    }
    payLegalFacade.lazyInitNewDocument(currentDoc, exFields);
  }

  public UmtPayLegal getCurrentDoc() {
    return currentDoc;
  }

  private void addCommonParamTooltip(UIComponent paramsContainer, UIOutput paramValueOutput, UmtPayLegalExField param) {
    Tooltip tooltip = new Tooltip();
    tooltip.setFor(paramValueOutput.getId());
    tooltip.setValue(Strings.isNullOrEmpty(param.getDescription()) ? param.getExTitle() : param.getDescription());
    tooltip.setShowEvent("focus");
    tooltip.setHideEvent("blur");
    paramsContainer.getChildren().add(tooltip);
  }

  private void addCommonParam(UmtPayLegalExField param) {
    UIComponent paramsContainer =
        FacesContext.getCurrentInstance().getViewRoot().findComponent("payLegalForm:commonAdditionalParams");


    UIOutput paramValueOutput;
    if (param.getIsEditable() == 0) {
      paramValueOutput = new OutputLabel();
      ((OutputLabel) paramValueOutput).setStyleClass("elem-add_param");
      paramValueOutput.setValue(String.format("%s: %s", param.getExTitle(), param.getExValue()));
    } else if (param.getChoiceValuesXML() != null && param.getChoiceValuesXML().getPayParams() != null
        && param.getChoiceValuesXML().getPayParams().size() != 0) {
      OutputLabel label = new OutputLabel();
      label.setValue(param.getExTitle());
      paramsContainer.getChildren().add(label);

      paramValueOutput = new SelectOneMenu();
      SelectOneMenu choiceOutput = (SelectOneMenu) paramValueOutput;
      for (UmtPayLegalExFieldParam payParam : param.getChoiceValuesXML().getPayParams()) {
        UISelectItem selectItem = new UISelectItem();
        selectItem.setItemLabel(payParam.getValue());
        selectItem.setItemValue(payParam.getKey());
        choiceOutput.getChildren().add(selectItem);
      }
      choiceOutput.setStyleClass("elem-add_param");
      paramValueOutput.setId("add_param_" + param.getExName());
      addCommonParamTooltip(paramsContainer, paramValueOutput, param);
    } else {
      OutputLabel label = new OutputLabel();
      label.setValue(param.getExTitle());
      paramsContainer.getChildren().add(label);

      if (param.getStep() == 0) {
        UMTWebProviderParam providerParam =
            providerFacade.getParamByName(currentDoc.getProvider(), param.getExName());

        if (Strings.isNullOrEmpty(providerParam.getMask())) {
          paramValueOutput = new InputText();
          if (providerParam.getMaxLength() != null && providerParam.getMaxLength() > 0) {
            ((InputText) paramValueOutput).setMaxlength(providerParam.getMaxLength());
          }
        } else {
          paramValueOutput = new InputMask();
          ((InputMask) paramValueOutput).setMask(providerParam.getMask());
        }
      } else {
        paramValueOutput = new InputText();
      }

      ((HtmlInputText) paramValueOutput).setStyleClass("elem-add_param");
      paramValueOutput.setId("add_param_" + param.getExName());
      addCommonParamTooltip(paramsContainer, paramValueOutput, param);
      paramValueOutput.setValue(param.getExValue());
    }
    FacesContext context = FacesContext.getCurrentInstance();
    ValueExpression expr = context.getApplication().getExpressionFactory().createValueExpression(
        context.getELContext(), "#{payLegalController.isDisabledCommonInputParams(" + param.getStep() + ")}",
        boolean.class);
    paramValueOutput.setValueExpression("disabled", expr);
    paramsContainer.getChildren().add(paramValueOutput);
    paramComponentBinding.put(param, paramValueOutput.getClientId());
  }

  public void addCommonParams() {
    if (isAdditionalParamsInited) {
      return;
    }
    for (UmtPayLegalExField param : exFields.getRecList()) {
      addCommonParam(param);
    }
    isAdditionalParamsInited = true;
  }

  public List<Country> autoCompleteCountry(String query) {
    if (!StringUtils.isEmpty(query)) {
      return countryFacade.getListStartWith(query, MAX_COUNTRY_LIST_SIZE);
    } else {
      return countryFacade.getAll();
    }
  }

  public List<UmtTransferClient> findSenderClientByCardNumber(String query) {
    return transferClientFacade.getListCardNumberStartWith(query, MAX_CLIENT_SIZE);
  }

  public void senderClientSearchSelected(SelectEvent event) {
    UmtTransferClient selectedClient = transferClientFacade.getById(Integer.parseInt((String) event.getObject()));
    currentDoc.setSenderCardNumber(selectedClient.getCardNumber());
    currentDoc.setSenderFirstName(selectedClient.getFirstName());
    currentDoc.setSenderLastName(selectedClient.getLastName());
    currentDoc.setSenderPatronymic(selectedClient.getPatronymic());
    currentDoc.setSenderIsResident(selectedClient.getIsResident());
    currentDoc.setSenderNationalityCode(selectedClient.getNationalityCode());
    currentDoc.setSenderBirthday(selectedClient.getBirthday());
    currentDoc.setSenderBirthPlace(selectedClient.getBirthPlace());
    currentDoc.setSenderINN(selectedClient.getINN());

    currentDoc.setSenderCountryISO(selectedClient.getCountryISO());
    currentDoc.setSenderCity(selectedClient.getCity());
    currentDoc.setSenderStreet(selectedClient.getStreet());
    currentDoc.setSenderHouse(selectedClient.getHouse());
    currentDoc.setSenderFlat(selectedClient.getFlat());
    currentDoc.setSenderPhone(selectedClient.getPhone());
    currentDoc.setSenderEmail(selectedClient.getEmail());

    currentDoc.setSenderLicenceType(selectedClient.getLicenceType());
    currentDoc.setSenderLicenceSeries(selectedClient.getLicenceSeries());
    currentDoc.setSenderLicenceNumber(selectedClient.getLicenceNumber());
    currentDoc.setSenderLicenceIssuer(selectedClient.getLicenceIssuer());
    currentDoc.setSenderLicenceDateIssue(selectedClient.getLicenceDateIssue());
    currentDoc.setSenderLicenceIssuer(selectedClient.getLicenceIssuer());
    currentDoc.setSenderId(selectedClient.getId());
  }

  public String getSenderMainDocumentAsString() {
    StringBuilder builder = new StringBuilder();
    Joiner.on(" ").skipNulls().
        appendTo(builder, currentDoc.getSenderLicenceType(), currentDoc.getSenderLicenceSeries(), currentDoc.getSenderLicenceNumber());

    if (!Strings.isNullOrEmpty(currentDoc.getSenderLicenceIssuer())) {
      if (!builder.toString().isEmpty()) {
        builder.append(", ");
      }
      if (currentDoc.getSenderLicenceIssuer() != null || currentDoc.getSenderLicenceDateIssue() != null) {
        builder.append("выдан");
        if (currentDoc.getSenderLicenceDateIssue() != null) {
          builder.append(" ").append(DatesUtils.formatDMY(currentDoc.getSenderLicenceDateIssue()));
        }
        if (currentDoc.getSenderLicenceIssuer() != null) {
          builder.append(" ").append(currentDoc.getSenderLicenceIssuer());
        }
      }
    }
    if (!Strings.isNullOrEmpty(currentDoc.getSenderLicenceDepCode())) {
      if (!builder.toString().isEmpty()) {
        builder.append(", ");
      }
      builder.append("код подр. ").append(currentDoc.getSenderLicenceDepCode());
    }
    if (currentDoc.getSenderLicenceExpireDate() != null) {
      if (!builder.toString().isEmpty()) {
        builder.append(", ");
      }
      builder.append("действует по ").append(DatesUtils.formatDMY(currentDoc.getSenderLicenceExpireDate()));
    }
    return builder.toString();
  }

  public String getSenderOtherDocumentAsString() {
    StringBuilder builder = new StringBuilder();
    if (!Strings.isNullOrEmpty(currentDoc.getSenderOtherDocType())) {
      builder.append(currentDoc.getSenderOtherDocType());
    }
    if (!Strings.isNullOrEmpty(currentDoc.getSenderOtherDocNum())) {
      builder.append(" ").append(currentDoc.getSenderOtherDocNum());
    }
    if (currentDoc.getSenderOtherDocDateStart() != null || currentDoc.getSenderOtherDocDateFinish() != null) {
      builder.append(", действует");
      if (currentDoc.getSenderOtherDocDateStart() != null) {
        builder.append(" с ").append(DatesUtils.formatDMY(currentDoc.getSenderOtherDocDateStart()));
      }
      if (currentDoc.getSenderOtherDocDateFinish() != null) {
        builder.append(" по ").append(DatesUtils.formatDMY(currentDoc.getSenderOtherDocDateFinish()));
      }
    }
    return builder.toString();
  }

  private String addPrefix(String prefix, String original){
    if ( Strings.isNullOrEmpty(original)) {
      return null;
    }
    else
      return prefix.concat(original);
  };

  private void addToList(List<String> list, String item){
    if ( !Strings.isNullOrEmpty(item)) {
      list.add(item);
    }
  }

  public String getSenderAddressAsString(){
    List<String> list = new ArrayList<String>();
    addToList(list, currentDoc.getSenderPostCode());
    if (currentDoc.getSenderCountryISO() != null) {
      addToList(list, currentDoc.getSenderCountryISO().getNameShortRu());
    }
    addToList(list, currentDoc.getSenderCity());
    addToList(list, currentDoc.getSenderStreet());
    addToList(list, addPrefix("д. ", currentDoc.getSenderHouse()));
    addToList(list, addPrefix("корп. ", currentDoc.getSenderHouseBlock()));
    addToList(list, addPrefix("стр. ", currentDoc.getSenderHouseBuilding()));
    addToList(list, addPrefix("кв. ", currentDoc.getSenderFlat()));
    return Joiner.on(", ").join(list);

  }

  private String removeParamMask(String value, String mask) {
    if (Strings.isNullOrEmpty(value)) {
      return value;
    }
    StringBuilder resultValue = new StringBuilder();
    for (int i = 0; i < mask.length(); i++) {
      if (mask.charAt(i) == '*') {
        resultValue.append(value.charAt(i));
      }
    }
    return resultValue.toString();
  }

  private void setExFieldsValuesFromForm() {
    UIViewRoot viewRoot = FacesContext.getCurrentInstance().getViewRoot();
    for (Map.Entry<UmtPayLegalExField, String> entry : paramComponentBinding.entrySet()) {
      if (entry.getKey().getIsEditable() == 0) {
        continue;
      }
      String paramValue;
      if (viewRoot.findComponent(entry.getValue()) instanceof HtmlInputText) {
        HtmlInputText currentAddInput = (HtmlInputText) viewRoot.findComponent(entry.getValue());
        paramValue = Strings.nullToEmpty((String) currentAddInput.getValue());
        if (currentAddInput instanceof InputMask) {
          paramValue = removeParamMask(paramValue, ((InputMask) currentAddInput).getMask());
        }
      } else {
        SelectOneMenu selectComponent = (SelectOneMenu) viewRoot.findComponent(entry.getValue());
        paramValue = (String) selectComponent.getValue();
      }
      entry.getKey().setExValue(paramValue);
    }
  }

  public void saveDocument() {
    try {
      List<ProcessStepResult> saveResult = saveDocumentLocal(forceSave);
      if (ProcessStepResult.hasError(saveResult) || ProcessStepResult.hasWarning(saveResult)) {
        resultController.processResult(saveResult);
        return;
      }

      if (Objects.equals(currentDoc.getProvider().getPaymentType(), PROVIDER_PAYMENT_TYPE_MULTI_STEP)
          && !Objects.equals(currentDoc.getStepsLeft(), 0)) {
        FindCheckResult findCheckResult = payLegalFacade.makeMultiStepActionInExternalSystem(currentDoc, exFields);
        if (findCheckResult.getResult().getType() != PROCESS_RESULT_OK) {
          resultController.processResult(findCheckResult.getResult());
          return;
        }
        for (UmtPayLegalExField addExField : findCheckResult.getAdditionalPayRequisites()) {
          addCommonParam(addExField);
          exFields.getRecList().add(addExField);
        }
        if (!Objects.equals(currentDoc.getStepsLeft(), 0) ||
            payLegalFacade.hasEditableFields(exFields, currentDoc.getCurrentStep())) {
          return;
        }
      }

      ProcessStepResult prepareStepResult = payLegalFacade.prepareDocInExternalSystem(currentDoc, exFields);
      resultController.processResult(prepareStepResult);
    } catch (Exception ex) {
      ex.printStackTrace();
      resultController.processResult(new ProcessStepResult("Сервис временно недоступен", PROCESS_RESULT_ERROR));
    }
  }

  private void postProcessSaveDocumentResults(List<ProcessStepResult> results, boolean isForceSave,
                                              final String processButtonCaption) {
    if (!ProcessStepResult.hasError(results) && ProcessStepResult.hasWarning(results) && !isForceSave) {
      forceSave = true;
      results.add(new ProcessStepResult(
          String.format("Продолжите редактирование или повторно нажмите кнопку \"%s\"", processButtonCaption),
          PROCESS_RESULT_WARNING));
    }
  }

  private List<ProcessStepResult> saveDocumentLocal(boolean isForceSave) {
    setExFieldsValuesFromForm();
    List<ProcessStepResult> results = payLegalFacade.saveDocumentLocal(currentDoc, exFields, isForceSave);
    postProcessSaveDocumentResults(results, isForceSave, "Сохранить");
    return results;
  }

  public String deleteDocument() {
    payLegalFacade.deleteDoc(currentDoc);
    return "ProviderList.xhtml";
  }

  public void postSaveProcessDocument() {
    if (!payLegalFacade.isDocSigned(currentDoc)) {
      List<ProcessStepResult> saveResult = payLegalFacade.saveDocumentLocal(currentDoc, exFields, forceSave);
      postProcessSaveDocumentResults(saveResult, forceSave, "Подтвердить");
      resultController.processResult(saveResult);

      if (!ProcessStepResult.hasError(saveResult) && !ProcessStepResult.hasWarning(saveResult)) {
        RequestContext.getCurrentInstance().execute("signDocument()");
      }
    } else {
      List<ProcessStepResult> resultList = payLegalFacade.confirmDocument(currentDoc, forceSave);
      postProcessSaveDocumentResults(resultList, forceSave, "Подтвердить");
      resultController.processResult(resultList);
      currentDoc = payLegalFacade.findDoc(currentDoc.getDocPK());
    }
  }

  private boolean isValidDocSignMessage(String message) {
    return message.matches("(?s).*Подпись успешно изменена для 1 документa.*");
  }

  private ProcessStepResult checkSignResult() {
    Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    int resCode = Integer.valueOf(params.get("resCode"));
    if (resCode != RTS_RESPONSE_RES_CODE_ERROR) {
      return new ProcessStepResult("Подпись документа не изменена", PROCESS_RESULT_ERROR);
    }

    String message = params.get("message");
    if (!isValidDocSignMessage(message)) {
      StringBuilder resMessageBuilder = new StringBuilder("Ошибка при подписи документа:\n");
      Pattern errMsgPattern = Pattern.compile("0\\|\\d=\\d{0,5}: (.*)");
      for (String oneMessage : message.split("\n")) {
        Matcher matcher = errMsgPattern.matcher(oneMessage);
        if (matcher.matches()) {
          resMessageBuilder.append(matcher.group(1).replaceAll("\\[BR\\]", "\n")).append("\n");
        }
      }
      return new ProcessStepResult(resMessageBuilder.toString(),
          PROCESS_RESULT_ERROR);
    }
    return EMPTY_OK;
  }

  public void processSignDocument() {
    ProcessStepResult result = checkSignResult();
    if (result.getType() != PROCESS_RESULT_OK) {
      resultController.processResult(result);
      return;
    }

    List<ProcessStepResult> confirmResult = payLegalFacade.confirmDocument(currentDoc, forceSave);
    postProcessSaveDocumentResults(confirmResult, forceSave, "Подтвердить");
    resultController.processResult(confirmResult);
    if (ProcessStepResult.hasError(confirmResult)) {
      return;
    }

    resultController.processResult(payLegalFacade.changeCurrentLimit(currentDoc));
  }

  public boolean isShowSave() {
    return !isView && payLegalFacade.isAvailableSave(currentDoc);
  }

  public boolean isShowDelete() {
    return !isView && payLegalFacade.isAvailableDelete(currentDoc);
  }

  public boolean isShowConfirm() {
    return !isView && payLegalFacade.isAvailableConfirm(currentDoc);
  }

  public boolean isShowCommission() {
    return !payLegalFacade.isAvailableSave(currentDoc);
  }

  public boolean isShowPrintRequest() {
    return !payLegalFacade.isAvailableSave(currentDoc);
  }

  public boolean isShowExport() {
    return !payLegalFacade.isAvailableSave(currentDoc);
  }

  public String getStatusName() {
    int status = currentDoc.getStatus();
    return transferFacade.getICStatusDescriptionByStatus(status, userSession.getUserLocale());
  }

  public boolean isDisabledCommonInputParams(Integer step) {
    return isView || !payLegalFacade.isAvailableSave(currentDoc) || !Objects.equals(currentDoc.getCurrentStep(), step);
  }

  public boolean isDisabledSenderInfo() {
    return isView || payLegalFacade.isDocSigned(currentDoc);
  }

  public String getClientDocumentAsShortString(UmtTransferClient client) {
    StringBuilder builder = new StringBuilder();
    Joiner.on(" ").skipNulls().
        appendTo(builder, client.getLicenceType(), client.getLicenceSeries(), client.getLicenceNumber());
    if (client.getLicenceDateIssue() != null) {
      builder.append(", выдан ").append(DatesUtils.formatDMY((client.getLicenceDateIssue())));
    }
    return builder.toString();
  }

  public String getAmountRange() {
    StringBuilder builder = new StringBuilder();
    if (currentDoc.getProvider().getMaxAmount() != null) {
      builder.append("от ").append(new Money(currentDoc.getProvider().getMinAmount()));
    }
    if (currentDoc.getProvider().getMinAmount() != null) {
      builder.append(" до ").append(new Money(currentDoc.getProvider().getMaxAmount()));
    }
    return builder.toString();
  }

  public String getDocCommission() {
    return new Money(currentDoc.getCommission(), RUR).toString();
  }

  public Float getMaxAmount() {
    return currentDoc.getProvider().getMaxAmount();
  }

  public int getAgentId() {
    return userSession.getAgentId();
  }

  public String getProviderDestinationCountries() {
    List<String> countries = new ArrayList<>();
    for (UMTWebProviderCountry country : currentDoc.getProvider().getCountries()) {
      countries.add(countryFacade.getByCode(country.getCountryCode()).getNameShortRu());
    }
    return Joiner.on(", ").join(countries);
  }

  public boolean isUseSenderAccount() {
    return isUseSenderAccount || !Strings.isNullOrEmpty(currentDoc.getSenderAccount());
  }

  public void setUseSenderAccount(boolean isUseSenderAccount) {
    this.isUseSenderAccount = isUseSenderAccount;
  }

  public boolean isRenderSenderAccountPanel() {
    return agentFacade.getCanWriteOffAccount(userSession.getAgentId());
  }

  public void instanceSave() {
    saveDocumentLocal(true);
  }

  public void updateDocumentDataWithCheck() {
    if (payLegalFacade.isDocSigned(currentDoc)) {
      return;
    }
    List<ProcessStepResult> resultList = saveDocumentLocal(true);
    if (ProcessStepResult.hasError(resultList)) {
      RequestContext.getCurrentInstance().addCallbackParam("validationFailed", true);
    }
    for (ProcessStepResult result : resultList) {
      if (result.getType() != PROCESS_RESULT_WARNING) {
        resultController.processResult(resultList);
      }
    }
  }

  public boolean isForceSave() {
    return forceSave;
  }

  public void setForceSave(boolean forceSave) {
    this.forceSave = forceSave;
  }

  public boolean isInstanceSave() {
    return !payLegalFacade.isDocSigned(currentDoc) && (applicationSettingsBean.getAutoSaveSec() > 0);
  }
}

