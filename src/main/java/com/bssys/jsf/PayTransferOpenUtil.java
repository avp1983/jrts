package com.bssys.jsf;

import com.bssys.bls.types.DboDocPK;
import com.bssys.conversion.Money;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.entity.UmtPayTransferOpen;
import com.bssys.session.UserSessionBean;
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

@Named("payTransferOpenUtil")
@FacesComponent(value = "PayTransferOpenUtil")
public class PayTransferOpenUtil extends Panel implements NamingContainer {
  @Inject
  private UmtPayTransferOpenFacade transferFacade;

  @Inject
  private com.bssys.ejb.UmtAgentFacade agentFacade;

  @Inject
  private UserSessionBean userSession;

  private OutputLabel docNumberLabel;
  private OutputLabel senderInfoLabel;
  private OutputLabel receiverInfoLabel;
  private OutputLabel statusLabel;
  private OutputLabel amountLabel;

  @Override
  public String getFamily() {
    return UINamingContainer.COMPONENT_FAMILY;
  }

  @Override
  public void encodeBegin(FacesContext context) throws IOException {
    String docIDR = (String) getAttributes().get("widgetData");
    UmtPayTransferOpen doc = transferFacade.getDocumentByID(new DboDocPK(docIDR));
    docNumberLabel.setValue(doc.getDocumentNumber());
    senderInfoLabel.setValue(Joiner.on(" ").skipNulls().join(doc.getSenderlastname(), doc.getSenderfirstname()));
    receiverInfoLabel.setValue(Joiner.on(" ").skipNulls().join(doc.getReceiverlastname(), doc.getReceiverfirstname()));
    statusLabel.setValue(transferFacade.getICStatusDescriptionByStatus(doc.getStatus(), userSession.getUserLocale()));
    amountLabel.setValue(new Money(doc.getAmount(), doc.getCurrcodeiso()).toString());
    super.encodeBegin(context);
  }

  public OutputLabel getDocNumberLabel() {
    return docNumberLabel;
  }

  public void setDocNumberLabel(OutputLabel docNumberLabel) {
    this.docNumberLabel = docNumberLabel;
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

  public OutputLabel getReceiverInfoLabel() {
    return receiverInfoLabel;
  }

  public void setReceiverInfoLabel(OutputLabel receiverInfoLabel) {
    this.receiverInfoLabel = receiverInfoLabel;
  }

  public OutputLabel getSenderInfoLabel() {
    return senderInfoLabel;
  }

  public void setSenderInfoLabel(OutputLabel senderInfoLabel) {
    this.senderInfoLabel = senderInfoLabel;
  }
}
