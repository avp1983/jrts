package com.bssys.jsf;

import com.bssys.cdi.utils.CdiBeanResolver;
import com.bssys.ejb.DashboardState;
import com.bssys.entity.DashboardWidgetModel;
import com.bssys.jsf.Widget.DashboardWidgetController;
import com.bssys.session.UserSessionBean;
import org.primefaces.component.dashboard.Dashboard;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named("DashboardController")
public abstract class DashboardController implements Serializable {
  private static final long serialVersionUID = 1L;
  private DashboardState dashboardState;

  private final Map<String, DashboardWidgetController> widgetControllers = new HashMap<>();

  @Inject
  private UserSessionBean userSession;
  @Inject
  private CdiBeanResolver beanResolver;

  private int userKey;

  private int lastActiveColumn = -1;

  protected abstract void saveDashboardState(int userKey, DashboardState dashboardState);

  protected abstract DashboardState restoreDashboardState(int userKey);

  protected DashboardState getDashboardState() {
    return dashboardState;
  }

  protected abstract Dashboard getDashboard();

  protected abstract void registerPossibleWidgetControllers();


  @PostConstruct
  private void initDashboard() {
    userKey = userSession.getUserKey();
    registerPossibleWidgetControllers();
  }

  protected void registerWidgetController(String beanName) {
    widgetControllers.put(beanName, beanResolver.getBeanByNameAndClass(beanName, DashboardWidgetController.class));
  }

  @PreDestroy
  public void saveDashboardStateOnDestroy() {
    if (dashboardState != null) {
      HashMap<String, DashboardWidgetModel> widgetModels = dashboardState.getWidgetModels();
      if (widgetModels != null) {
        List<String> widgetToDelete = new ArrayList<>();
        for (Map.Entry<String, DashboardWidgetModel> widgetModelEntry : widgetModels.entrySet()) {
          if (widgetModelEntry.getValue().isRenderFail()) {
            widgetToDelete.add(widgetModelEntry.getKey());
          }
        }
        for (String key: widgetToDelete){
          widgetModels.remove(key);
        }
      }
      saveDashboardState(userKey, dashboardState);
    }
  }

  @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
  protected void processNewWidget(@SuppressWarnings("LocalCanBeFinal") Panel widget) {
  }

  private void processCreatedWidget(Panel widget, String widgetId) {
    widget.setId(widgetId);
    processNewWidget(widget);
  }

  private Panel createFailWidget(final String widgetId) {
    Panel widget = new Panel();
    processCreatedWidget(widget, "fail" + widgetId);
    widget.setHeader(userSession.getLocRes("dashboard", "DASHBOARD_FAIL_WIDGET_HEADER"));
    return widget;
  }

  private Panel createWidget(final String widgetId, final DashboardWidgetModel widgetModel) {
    if (widgetModel.isRenderFail()){
      return createFailWidget(widgetId);
    }

    Panel widget;
    try {
      DashboardWidgetController controller = widgetControllers.get(widgetModel.getType());
      widget = controller.createWidget(widgetModel.getData());
      processCreatedWidget(widget, widgetId);
    } catch (Exception ex) {
      Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, "Ошибка при создании виджета", ex);
      widgetModel.setRenderFail(true);
      return createFailWidget(widgetId);
    }
    return widget;
  }

  private void addDynamicChildrenToDashBoard() {
    getDashboard().getChildren().clear();
    for (Map.Entry<String, DashboardWidgetModel> entry : dashboardState.getWidgetModels().entrySet()) {
      getDashboard().getChildren().add(createWidget(entry.getKey(), entry.getValue()));
    }
  }

  public boolean hasWidgetWithControllerClass(final String widgetControllerBeanName) {
    for (DashboardWidgetModel widgetModel : dashboardState.getWidgetModels().values()) {
      if (widgetModel.getType().equals(widgetControllerBeanName)) {
        return true;
      }
    }
    return false;
  }

  public void removeWidget(final String widgetControllerBeanName) {
    HashMap<String, DashboardWidgetModel> widgetModels = dashboardState.getWidgetModels();
    List<String> widgetToDelete = new ArrayList<>();
    for (Map.Entry<String, DashboardWidgetModel> widgetModelEntry : widgetModels.entrySet()) {
      if (widgetModelEntry.getValue().getType().equals(widgetControllerBeanName)) {
        widgetToDelete.add(widgetModelEntry.getKey());
      }
    }
    for (String key: widgetToDelete){
      widgetModels.remove(key);
    }
  }

  private int getActiveColumn() {
    if ((lastActiveColumn >= dashboardState.getDashboardModel().getColumnCount() - 1) || (lastActiveColumn < 0)) {
      lastActiveColumn = 0;
    } else {
      lastActiveColumn++;
    }
    return lastActiveColumn;
  }

  protected void addWidget(final String widgetControllerBeanName, int columnNumber) {
    DashboardWidgetController controller = widgetControllers.get(widgetControllerBeanName);
    if (controller == null) {
      throw new RuntimeException(
          "widgetControllers.get error when widgetControllerBeanName = " + widgetControllerBeanName
              + " maybe you forget call registerPossibleWidgetControllers?");
    }
    DashboardWidgetModel widgetModel =
        new DashboardWidgetModel(widgetControllerBeanName,
            controller.getWidgetDataFromRequest());
    if (dashboardState.getWidgetModels().containsValue(widgetModel)) {
      return;
    }

    UIComponent component = createWidget(widgetControllerBeanName
        + UUID.randomUUID().toString(), widgetModel);
    getDashboard().getChildren().add(component);
    dashboardState.getWidgetModels().put(component.getId(), widgetModel);

    DashboardColumn column = dashboardState.getDashboardModel().getColumn(columnNumber);
    column.addWidget(column.getWidgetCount(), component.getId());
    lastActiveColumn = columnNumber;
  }

  protected void addWidget(final String widgetControllerBeanName) {
    addWidget(widgetControllerBeanName, getActiveColumn());
  }

  @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
  protected void processDashboardInitialState() {

  }

  @SuppressWarnings("PMD.EmptyMethodInAbstractClassShouldBeAbstract")
  protected void processDashboardRestoredState() {

  }

  public DashboardModel getModel() {
    if (dashboardState == null) {
      dashboardState = restoreDashboardState(userKey);

      if (dashboardState != null) {
        processDashboardRestoredState();
      } else {
        dashboardState = new DashboardState();
        processDashboardInitialState();
        return dashboardState.getDashboardModel();
      }
    }

    if (FacesContext.getCurrentInstance().getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {
      addDynamicChildrenToDashBoard();
    }
    return dashboardState.getDashboardModel();
  }

}
