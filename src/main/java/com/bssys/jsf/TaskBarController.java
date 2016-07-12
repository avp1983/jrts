package com.bssys.jsf;

import com.bssys.ejb.DashboardState;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UserInterfaceStateFacade;
import com.bssys.entity.DashboardWidgetModel;
import com.bssys.session.UserSessionBean;
import org.primefaces.behavior.ajax.AjaxBehavior;
import org.primefaces.behavior.ajax.AjaxBehaviorListenerImpl;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DefaultDashboardColumn;

import javax.el.MethodExpression;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.*;

@Named("taskBarController")
@SessionScoped
public class TaskBarController extends DashboardController {
  @Inject
  private UserInterfaceStateFacade userInterfaceStateFacade;

  @Inject
  private UserRightController userRightController;

  @Inject
  private UmtAgentFacade agentFacade;

  @Inject
  private UserSessionBean userSession;

  protected void saveDashboardState(int userKey, DashboardState dashboardState) {
    userInterfaceStateFacade.saveTaskBarToDB(userKey, dashboardState);
  }

  @Override
  protected DashboardState restoreDashboardState(int userKey) {
    return userInterfaceStateFacade.getTaskBarStateFromDB(userKey);
  }

  @Override
  protected void registerPossibleWidgetControllers() {
    registerWidgetController(IMPORTANT_WIDGET);
    registerWidgetController(PAY_TRANSFER_OPEN);
    registerWidgetController(PAY_TRANSFER_CLOSE);
    registerWidgetController(FIND_TRANSFER);
    registerWidgetController(LAST_TRANSFER_MINI);
    registerWidgetController(TRANSFER_COUNT);
    registerWidgetController(FREE_CLIENT_DOC);
    registerWidgetController(FREE_AG_EX_DOC);
    registerWidgetController(ADDITIONAL_COMMISSION_WIDGET);
    registerWidgetController(PAY_LEGAL);
  }

  @SuppressWarnings("UnusedDeclaration")
  public void removeWidget(AjaxBehaviorEvent event) {
    UIComponent component = event.getComponent();
    getDashboard().getChildren().remove(component);
    getDashboardState().getWidgetModels().remove(component.getId());
    getDashboardState().getDashboardModel().getColumn(0).removeWidget(component.getId());
  }

  @Override
  protected void processNewWidget(Panel widget) {
    widget.setClosable(true);
    FacesContext context = FacesContext.getCurrentInstance();
    MethodExpression me = context.getApplication().getExpressionFactory().createMethodExpression(context.getELContext(),
        "#{taskBarController.removeWidget}", null, new Class[]{AjaxBehaviorEvent.class});
    AjaxBehavior ajaxBehavior = new AjaxBehavior();
    AjaxBehaviorListenerImpl ajaxBehaviorImpl = new AjaxBehaviorListenerImpl(me, me);
    ajaxBehavior.addAjaxBehaviorListener(ajaxBehaviorImpl);
    widget.addClientBehavior("close", ajaxBehavior);
  }

  @Override
  protected void processDashboardInitialState() {
    getDashboardState().getDashboardModel().addColumn(new DefaultDashboardColumn());
  }

  public boolean isActiveAddCommissionsWidget() {
    return agentFacade.isRootAgent(userSession.getAgentId()) && userRightController.userHasRightAdditionalCommission();
  }

  public boolean isActiveTransferWidgets() {
    return userRightController.userHasRightTransferAction();
  }

  public boolean isActiveTransferWidgetsPay() {
    return userRightController.userHasRightTransferActionAndPay();
  }

  public boolean isActiveTransferViewWidgets() {
    return userRightController.userHasRightTransferViewOrAction();
  }

  @Override
  protected void processDashboardRestoredState() {
    HashMap<String, DashboardWidgetModel> widgetModels = getDashboardState().getWidgetModels();
    Iterator iter = widgetModels.entrySet().iterator();
    while (iter.hasNext()) {
      Map.Entry<String, DashboardWidgetModel> widgetEntry = (Map.Entry<String, DashboardWidgetModel>) iter.next();
      String currWidgetType = widgetEntry.getValue().getType();
      if (((TRANSFER_WIDGETS.contains(currWidgetType) && !isActiveTransferWidgets())) ||
          ((currWidgetType.equals(FIND_TRANSFER) && !isActiveTransferWidgetsPay())) ||
          ((ADDITIONAL_COMMISSION_WIDGET.equals(currWidgetType) && !isActiveAddCommissionsWidget()))) {
        iter.remove();
      }
    }
  }

  private String getWidgetByScheme(String schemeName) {
    if (0 == schemeName.compareToIgnoreCase("UMTPAYTRANSFEROPEN")) {
      return PAY_TRANSFER_OPEN;
    } else if (0 == schemeName.compareToIgnoreCase("UMTPAYTRANSFERCLOSE")) {
      return PAY_TRANSFER_CLOSE;
    } else if (0 == schemeName.compareToIgnoreCase("FREECLIENTDOC")) {
      return FREE_CLIENT_DOC;
    } else if (0 == schemeName.compareToIgnoreCase("FREEAGEXDOC")) {
      return FREE_AG_EX_DOC;
    } else if (0 == schemeName.compareToIgnoreCase("UMTPAYLEGAL")) {
      return PAY_LEGAL;
    } else {
      throw new RuntimeException("Не найден виджет для схемы = " + schemeName);
    }
  }

  public void addTransferCountWidget() {
    addWidget(TRANSFER_COUNT);
  }

  public void addDocWidget() {
    String scheme = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("schemename");
    String widgetControllerBeanName = getWidgetByScheme(scheme);
    if (widgetControllerBeanName != null) {
      addWidget(widgetControllerBeanName);
    }
  }

  public void addFindTransferWidget() {
    addWidget(FIND_TRANSFER);
  }

  public void addLastTransferWidget() {
    addWidget(LAST_TRANSFER_MINI);
  }

  public void addImportantWidget() {
    addWidget(IMPORTANT_WIDGET);
  }

  public void addAddCommissionWidget() {
    addWidget(ADDITIONAL_COMMISSION_WIDGET);
  }

  @Override
  protected Dashboard getDashboard() {
    return (Dashboard) FacesContext.getCurrentInstance().getViewRoot().findComponent("form:taskBoard");
  }

  public boolean isRenderTaskbarDasboard() {
    return !"1".equals(FacesContext.getCurrentInstance().getExternalContext().getRequestHeaderMap()
        .get("IsNotRenderDashboard"));
  }
}
