package com.bssys.jsf;

import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UserRightFacade;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.UserRightEnum;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Set;

import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS_LEGAL_ENTITIES;
import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS;
import static com.bssys.ejb.AgentAllowActions.CAN_PAY_TRANSFERS;
import static com.bssys.umt.UserRightEnum.*;

@Named("userRightController")
@SessionScoped
public class UserRightController implements Serializable {
  @Inject
  private UserSessionBean userSession;
  @Inject
  private UserRightFacade userRightFacade;
  @Inject
  private UmtAgentFacade agentFacade;

  Boolean agentHasRightPayLegal;
  Boolean agentHasRightTransferSend;
  Boolean agentHasRightTransferPay;

  private Set<Integer> userRights;

  private Set<Integer> getUserRights() {
    if (userRights == null) {
      userRights = userRightFacade.getUserRightItems(userSession.getUserKey());
    }
    return userRights;
  }

  public boolean userHasRight(UserRightEnum userRight) {
    return getUserRights().contains(userRight.getValue());
  }

  public boolean userHasRightTransferAction() {
    return userHasRight(USER_RIGHT_TRANSFER_ACTION);
  }

  public boolean userHasRightTransferViewOrAction() {
    return userHasRight(USER_RIGHT_TRANSFER_ACTION)
        || userHasRight(USER_RIGHT_TRANSFER_VIEW);
  }

  public boolean userHasRightTransferActionOrViewToPay() {
    return userHasRight(USER_RIGHT_TRANSFER_ACTION)
        || userHasRight(USER_RIGHT_TRANSFER_VIEW_TOPAY);
  }

  public boolean userHasRightReportOrReportMutual() {
    return userHasRight(USER_RIGHT_REPORT)
        || userHasRight(USER_RIGHT_REPORT_MUTUAL);
  }

  public boolean userHasRightSignUserRequest() {
    return userHasRight(USER_RIGHT_SIGN_USER_REQUEST);
  }

  public boolean userHasRightMessagesAdmin() {
    return userHasRight(USER_RIGHT_MESSAGE_ADMIN);
  }

  public boolean userHasRightMessagesAgent() {
    return userHasRight(USER_RIGHT_MESSAGE_AGENT);
  }

  public boolean userHasRightAdditionalCommission() {
    return userHasRight(USER_RIGHT_ADDITIONAL_COMMISSION);
  }

  public boolean getUserHasRightMutualSettlements() { return userHasRight(USER_RIGHT_REPORT_MUTUAL_SETTLEMENTS); }

  public boolean agentHasRightPayLegal() {
    if (agentHasRightPayLegal == null){
      agentHasRightPayLegal =
          agentFacade.getAgentAllowActions(userSession.getAgentId()).contains(CAN_SEND_TRANSFERS_LEGAL_ENTITIES);
    }
    return agentHasRightPayLegal;
  }

  public boolean agentHasRightTransferSend() {
    if (agentHasRightTransferSend == null){
      agentHasRightTransferSend =
          agentFacade.getAgentAllowActions(userSession.getAgentId()).contains(CAN_SEND_TRANSFERS);
    }
    return agentHasRightTransferSend;
  }

  public boolean agentHasRightTransferPay() {
    if (agentHasRightTransferPay == null){
      agentHasRightTransferPay =
          agentFacade.getAgentAllowActions(userSession.getAgentId()).contains(CAN_PAY_TRANSFERS);
    }
    return agentHasRightTransferPay;
  }

  public boolean userHasRightTransferActionAndSend() {
    return userHasRightTransferAction() && agentHasRightTransferSend();
  }

  public boolean userHasRightTransferActionAndPay() {
    return userHasRightTransferAction() && agentHasRightTransferPay();
  }

  public boolean userHasRightTransferActionAndSendOrPay() {
    return userHasRightTransferAction() && (agentHasRightTransferSend() || agentHasRightTransferPay());
  }

  public boolean userHasRightTransferActionAndPayLegal() {
    return userHasRightTransferAction() && agentHasRightPayLegal();
  }

  public boolean getUserHasRightReadyReport() {
    return userHasRight(USER_RIGHT_REPORT);
  }
}
