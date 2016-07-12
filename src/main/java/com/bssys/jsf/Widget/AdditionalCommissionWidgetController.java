package com.bssys.jsf.Widget;

import com.bssys.ejb.UmtAgentFacade;
import com.bssys.session.UserSessionBean;
import com.sun.tools.javac.util.Pair;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.concurrent.Callable;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.ADDITIONAL_COMMISSION_WIDGET;

@Named(ADDITIONAL_COMMISSION_WIDGET)
@ViewScoped
public class AdditionalCommissionWidgetController extends StatelessCompositeWidget {
  @Inject
  private UserSessionBean userSession;

  @Inject
  private UmtAgentFacade umtAgentFacade;

  private DashboardWidgetScore notAcceptedAdditionalCommissions;
  private DashboardWidgetScore activeAdditionalCommissions;

  @Override
  protected Pair<String, String> getLocResHeader() {
    return Pair.of("addcommwidget", "ADD_COMM_WIDGET_HEADER");
  }

  @PostConstruct
  public void start() {
    this.notAcceptedAdditionalCommissions = new DashboardWidgetScore(DashboardWidgetScoreName.NOT_ACCEPTED_ADDITIONAL_COMMISSIONS_COUNT,
        userSession.getUserKey(),
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getRejectedAdditionalCommissions(userSession.getAgentId());
          }
        }
    );
    this.activeAdditionalCommissions = new DashboardWidgetScore(DashboardWidgetScoreName.ACTIVE_ADDITIONAL_COMMISSIONS_COUNT,
        userSession.getUserKey(),
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getActiveAdditionalCommissions(userSession.getAgentId());
          }
        }
    );
  }

  @Override
  protected String getCompositeName() {
    return "addCommission";
  }

  @Override
  protected String getStyleClass() {
    return "ui-widget-scroller ui-widget-important";
  }

  public DashboardWidgetScore getNotAcceptedAdditionalCommissions() {
    this.notAcceptedAdditionalCommissions.updateData();
    return this.notAcceptedAdditionalCommissions;
  }

  public DashboardWidgetScore getActiveAdditionalCommissions() {
    this.activeAdditionalCommissions.updateData();
    return this.activeAdditionalCommissions;
  }

  public void resetValues() {
    notAcceptedAdditionalCommissions.reset();
    activeAdditionalCommissions.reset();
  }
}
