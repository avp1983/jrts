package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;
import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.process.step.ProcessStepResultController;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.ApplicationSettingsBean;
import com.google.common.base.Joiner;
import org.primefaces.component.outputlabel.OutputLabel;
import org.primefaces.component.panel.Panel;

import javax.faces.component.FacesComponent;
import javax.faces.component.NamingContainer;
import javax.faces.component.UINamingContainer;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.IOException;


@Named("payLegalUtil")
@FacesComponent(value = "PayLegalUtil")
public class PayLegalUtil extends Panel implements NamingContainer {

  @Inject
  private UmtPayLegalFacade payLegalFacade;
  @Inject
  private UmtPayTransferOpenFacade payTransferOpenFacade;
  @Inject
  private UserSessionBean userSession;
  @Inject
  private ProcessStepResultController resultController;
  @Inject
  private ApplicationSettingsBean dspSettingsBean;

  private OutputLabel docNumberLabel;
  private OutputLabel senderInfoLabel;
  private OutputLabel providerLabel;
  private OutputLabel statusLabel;
  private OutputLabel amountLabel;

  @Override
  public String getFamily() {
    return UINamingContainer.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    String docIDR = (String) getAttributes().get("widgetData");
    UmtPayLegal doc = payLegalFacade.getDocumentByID(new DboDocPK(docIDR));
    docNumberLabel.setValue(dspSettingsBean.isTcnInsteadOfDn() ? doc.getCheckNumber() : doc.getDocumentNumber());
    senderInfoLabel.setValue(Joiner.on(" ").skipNulls().join(doc.getSenderLastName(), doc.getSenderFirstName()));
    providerLabel.setValue(doc.getProviderName());
    statusLabel.setValue(
        payTransferOpenFacade.getICStatusDescriptionByStatus(doc.getStatus(), userSession.getUserLocale()));
    amountLabel.setValue(new Money(doc.getAmount(), doc.getCurrCodeIso()).toString());
    super.encodeBegin(context);
  }

  public OutputLabel getAmountLabel() {
    return amountLabel;
  }

  public void setAmountLabel(OutputLabel amountLabel) {
    this.amountLabel = amountLabel;
  }

  public OutputLabel getStatusLabel() {
    return statusLabel;
  }

  public void setStatusLabel(OutputLabel statusLabel) {
    this.statusLabel = statusLabel;
  }

  public OutputLabel getProviderLabel() {
    return providerLabel;
  }

  public void setProviderLabel(OutputLabel providerLabel) {
    this.providerLabel = providerLabel;
  }

  public OutputLabel getSenderInfoLabel() {
    return senderInfoLabel;
  }

  public void setSenderInfoLabel(OutputLabel senderInfoLabel) {
    this.senderInfoLabel = senderInfoLabel;
  }

  public OutputLabel getDocNumberLabel() {
    return docNumberLabel;
  }

  public void setDocNumberLabel(OutputLabel docNumberLabel) {
    this.docNumberLabel = docNumberLabel;
  }
}
