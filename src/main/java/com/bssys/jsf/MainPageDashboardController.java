package com.bssys.jsf;

import com.bssys.ejb.DashboardState;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UserInterfaceStateFacade;
import com.bssys.session.UserSessionBean;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.*;

@Named("mainPageDashboardController")
@SessionScoped
public class MainPageDashboardController extends DashboardController {
  @Inject
  private UserInterfaceStateFacade userInterfaceStateFacade;

  @Inject
  private UserRightController userRightController;

  @Inject
  private UmtAgentFacade agentFacade;

  @Inject
  private UserSessionBean userSession;

  @Override
  protected void saveDashboardState(int userKey, DashboardState dashboardState) {
    userInterfaceStateFacade.saveMainPageDashboardToDB(userKey, dashboardState);
  }

  @Override
  protected DashboardState restoreDashboardState(int userKey) {
    return userInterfaceStateFacade.getMainPageDashboardFromDB(userKey);
  }

  private void addWidgetIfAbsent(String widgetControllerBeanName) {
    if (!hasWidgetWithControllerClass(widgetControllerBeanName)) {
      addWidget(widgetControllerBeanName);
    }
  }

  private void setWidgetEnableInDashboard(String widgetControllerBeanName, boolean isEnable) {
    if (isEnable) {
      addWidgetIfAbsent(widgetControllerBeanName);
    } else {
      removeWidget(widgetControllerBeanName);
    }
  }

  @Override
  protected void processDashboardRestoredState() {
    addWidgetIfAbsent(IMPORTANT_WIDGET);
    setWidgetEnableInDashboard(FIND_TRANSFER, userRightController.userHasRightTransferActionAndPay());
    setWidgetEnableInDashboard(LAST_TRANSFER, userRightController.userHasRightTransferAction());
    setWidgetEnableInDashboard(TRANSFER_COUNT, userRightController.userHasRightTransferAction());
    setWidgetEnableInDashboard(ADDITIONAL_COMMISSION_WIDGET,
        agentFacade.isRootAgent(userSession.getAgentId()) && userRightController.userHasRightAdditionalCommission());
  }

  @Override
  protected void processDashboardInitialState() {
    DefaultDashboardModel dashboardModel = getDashboardState().getDashboardModel();
    dashboardModel.addColumn(new DefaultDashboardColumn());
    dashboardModel.addColumn(new DefaultDashboardColumn());
    dashboardModel.addColumn(new DefaultDashboardColumn());
    processDashboardRestoredState();
  }

  @Override
  protected Dashboard getDashboard() {
    return (Dashboard) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:mainPageDashboard");
  }

  @Override
  protected void registerPossibleWidgetControllers() {
    registerWidgetController(FIND_TRANSFER);
    registerWidgetController(LAST_TRANSFER);
    registerWidgetController(TRANSFER_COUNT);
    registerWidgetController(IMPORTANT_WIDGET);
    registerWidgetController(ADDITIONAL_COMMISSION_WIDGET);
  }
}
