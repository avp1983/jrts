package com.bssys.jsf;

import com.bssys.ejb.UmtPayLegalValidationSettingsBean;
import com.sun.faces.util.MessageFactory;

import javax.enterprise.context.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.EnumSet;

import static com.bssys.ejb.UmtPayLegalValidationSettingsBean.PayLegalValidationField;
import static com.bssys.ejb.UmtPayLegalValidationSettingsBean.PayLegalValidationField.*;

@Named("payLegalValidationController")
@RequestScoped
public class PayLegalValidationController implements Serializable {
  @Inject
  UmtPayLegalValidationSettingsBean validationSettingsBean;

  private Boolean isInstanceSave = null;

  public boolean isContainsRequiredField(EnumSet<PayLegalValidationField> fields) {
    if (!isCheckRequired()) {
      return false;
    }

    Boolean res = false;
    for (PayLegalValidationField field : fields) {
      res = res || validationSettingsBean.isRequiredField(field);
    }
    return res;
  }

  public boolean isRequiredField(PayLegalValidationField field) {
    return isCheckRequired() && validationSettingsBean.isRequiredField(field);
  }

  private boolean isCheckRequired() {
    if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE){
      return true;
    }
    if (isInstanceSave == null) {
      isInstanceSave = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap()
          .get("INSTANCE_SAVE_FLAG") != null;
    }
    return !isInstanceSave;
  }

  public boolean isRequiredAmount() {
    return isCheckRequired();
  }

  public boolean isRequiredMainDoc() {
    return isContainsRequiredField(EnumSet.of(SenderLicenceType, SenderLicenceSeries, SenderLicenceNumber,
        SenderLicenceDateIssue, SenderLicenceIssuer, SenderLicenceDepCode));
  }

  public boolean isRequiredOtherDoc() {
    return isContainsRequiredField(
        EnumSet.of(SenderOtherDocType, SenderOtherDocNum, SenderOtherDocDateStart, SenderOtherDocDateFinish));
  }

  private String getCurrentComponentLabel() {
    return (String) MessageFactory.getLabel(FacesContext.getCurrentInstance(),
        UIComponent.getCurrentComponent(FacesContext.getCurrentInstance()));
  }

  public String getMainDocFieldsRequiredMessage() {
    return String.format("Не заполнено поле \"Документ, удостоверяющий личность: %s\"", getCurrentComponentLabel());
  }

  public String getOtherDocFieldRequiredMessage() {
    return String.format("Не заполнено поле \"Миграционная карта или иной документ : %s\"", getCurrentComponentLabel());
  }

  }
