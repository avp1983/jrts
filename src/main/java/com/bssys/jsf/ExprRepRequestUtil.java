package com.bssys.jsf;

import com.bssys.ejb.ExprRepRequestFacade;
import com.bssys.session.UserSessionBean;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.math.BigDecimal;

@Named("exprRepRequestUtil")
@SessionScoped
public class ExprRepRequestUtil implements Serializable {
  private static final long serialVersionUID = 1L;
  private static final int REPORT_TYPE_MUTUAL_SETTLEMENT = 3;
  private static final int REPORT_TYPE_LOYALTY_ACTIONS = 40;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private ExprRepRequestFacade exprRepRequestFacade;

  public boolean userHasReportsMutualSettlements() {
    return (exprRepRequestFacade.getCountUserReportsOfType(userSession.getAgentId(), REPORT_TYPE_MUTUAL_SETTLEMENT)
            .compareTo(0) > 0);
  }

  public boolean userHasReportsLoyaltyActions() {
    return (exprRepRequestFacade.getCountUserReportsOfType(userSession.getAgentId(), REPORT_TYPE_LOYALTY_ACTIONS)
            .compareTo(0) > 0);
  }
}
