package com.bssys.ejb;

import com.bssys.entity.DashboardWidgetModel;
import org.primefaces.model.DefaultDashboardModel;

import java.io.Serializable;
import java.util.HashMap;

public class DashboardState implements Serializable {
  private static final long serialVersionUID = 1L;
  private final DefaultDashboardModel dashboardModel;
  private final HashMap<String, DashboardWidgetModel> widgetModels;

  public DashboardState() {
    dashboardModel = new DefaultDashboardModel();
    widgetModels = new HashMap<>();
  }

  public DefaultDashboardModel getDashboardModel() {
    return dashboardModel;
  }

  public HashMap<String, DashboardWidgetModel> getWidgetModels() {
    return widgetModels;
  }
}
