package com.bssys.jsf.Widget;

import com.bssys.ejb.UMTWebProviderCategoriesFacade;
import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.entity.UMTWebProviderHelper;
import com.bssys.session.UserSessionBean;
import com.sun.tools.javac.util.Pair;
import org.primefaces.event.SelectEvent;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;
import java.util.concurrent.Callable;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.IMPORTANT_WIDGET;

@Named(IMPORTANT_WIDGET)
@ViewScoped
public class ImportantWidgetController extends StatelessCompositeWidget {
  @Inject
  private UserSessionBean userSession;

  @Inject
  private UmtAgentFacade umtAgentFacade;

  @Inject
  private UMTWebProviderCategoriesFacade umtWebProviderCategoriesFacade;

  @Inject
  private UMTWebProviderFacade umtWebProviderFacade;

  private DashboardWidgetScore agentFinishedReportCount;
  private DashboardWidgetBigDecimalScore agentLoyalityPointsTotal;
  private DashboardWidgetBigDecimalScore agentCurrentLimit;
  private DashboardWidgetScore agentNewCertRequestCount;
  private DashboardWidgetScore agentNewMsgFromAdminCount;
  private DashboardWidgetScore agentNewMsgFromAgentsCount;
  private DashboardWidgetScore newsAddedTodayCount;

  private UMTWebProviderHelper provider;

  @Override
  protected Pair<String, String> getLocResHeader() {
    return Pair.of("importantwidget", "IMPORTANT_WIDGET_HEADER");
  }

  @PostConstruct
  public void start() {
    int userKey = userSession.getUserKey();

    this.agentFinishedReportCount = new DashboardWidgetScore(DashboardWidgetScoreName.AGENT_FINISHED_REPORT_COUNT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentFinishedReportCount(userSession.getAgentId());
          }
        }
    );

    this.agentLoyalityPointsTotal = new DashboardWidgetBigDecimalScore(DashboardWidgetScoreName.AGENT_LOYALITY_POINTS_TOTAL, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentLoyalityPointsTotal(userSession.getAgentId());
          }
        }
    );
    this.agentCurrentLimit = new DashboardWidgetBigDecimalScore(DashboardWidgetScoreName.AGENT_CURRENT_LIMIT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentCurrentLimit(userSession.getAgentId());
          }
        }
    );
    this.agentNewCertRequestCount = new DashboardWidgetScore(DashboardWidgetScoreName.AGENT_NEW_CERT_REQUEST_COUNT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentNewCertRequestCount(userSession.getAgentId());
          }
        }
    );
    this.agentNewMsgFromAdminCount = new DashboardWidgetScore(DashboardWidgetScoreName.AGENT_NEW_MSG_FROM_ADMIN_COUNT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentNewMsgFromAdminCount(userSession.getAgentId());
          }
        }
    );
    this.agentNewMsgFromAgentsCount = new DashboardWidgetScore(DashboardWidgetScoreName.AGENT_NEW_MSG_FROM_AGENTS_COUNT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getAgentNewMsgFromAgentsCount(userSession.getAgentId(), userSession.getUserKey());
          }
        }
    );
    this.newsAddedTodayCount = new DashboardWidgetScore(DashboardWidgetScoreName.NEWS_ADDED_TODAY_COUNT, userKey,
        new Callable<Object>() {
          @Override
          public Object call() throws Exception {
            return umtAgentFacade.getNewsAddedTodayCount();
          }
        }
    );
  }

  @Override
  protected String getCompositeName() {
    return "important";
  }

  @Override
  protected String getStyleClass() {
    return "ui-widget-scroller ui-widget-important";
  }

  public DashboardWidgetBigDecimalScore getAgentLoyalityPointsTotal() {
    this.agentLoyalityPointsTotal.updateData();
    return this.agentLoyalityPointsTotal;
  }

  public DashboardWidgetBigDecimalScore getAgentCurrentLimit() {
    this.agentCurrentLimit.updateData();
    return this.agentCurrentLimit;
  }

  public DashboardWidgetScore getAgentFinishedReportCount() {
    this.agentFinishedReportCount.updateData();
    return this.agentFinishedReportCount;
  }

  public DashboardWidgetScore getAgentNewCertRequestCount() {
    this.agentNewCertRequestCount.updateData();
    return this.agentNewCertRequestCount;
  }

  public DashboardWidgetScore getAgentNewMsgFromAdminCount() {
    this.agentNewMsgFromAdminCount.updateData();
    return this.agentNewMsgFromAdminCount;
  }

  public DashboardWidgetScore getAgentNewMsgFromAgentsCount() {
    this.agentNewMsgFromAgentsCount.updateData();
    return this.agentNewMsgFromAgentsCount;
  }

  public DashboardWidgetScore getNewsAddedTodayCount() {
    this.newsAddedTodayCount.updateData();
    return this.newsAddedTodayCount;
  }

  public Boolean hasAgentLoyalityActions() {
    return umtAgentFacade.hasAgentLoyalityActions(userSession.getAgentId());
  }

  public UMTWebProviderHelper getProvider() {
    return provider;
  }

  public void setProvider(UMTWebProviderHelper provider) {
    this.provider = provider;
  }

  public List<UMTWebProviderHelper> completeProvider(String searchString) {
    return umtWebProviderFacade.getProviders("%" + searchString + "%");
  }

  public void onItemSelect(SelectEvent event) {

  }

}
