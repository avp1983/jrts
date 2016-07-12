package com.bssys.jsf.Widget;

import org.primefaces.component.panel.Panel;

public interface DashboardWidgetController {
  Panel createWidget(Object widgetData);

  Object getWidgetDataFromRequest();
}
