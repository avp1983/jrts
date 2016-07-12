package com.bssys.jsf.Widget;

import com.bssys.session.UserSessionBean;
import com.sun.tools.javac.util.Pair;
import org.primefaces.component.panel.Panel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.HashMap;

public abstract class StatelessCompositeWidget implements DashboardWidgetController, Serializable {
  @Inject
  private UserSessionBean userSession;

  protected abstract Pair<String, String> getLocResHeader();

  protected abstract String getCompositeName();

  protected abstract String getStyleClass();

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    Pair<String, String> headerLocRes = getLocResHeader();
    widget.setHeader(userSession.getLocRes(headerLocRes.fst, headerLocRes.snd));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", getCompositeName(), new HashMap<String, Object>());
    widget.getChildren().add(component);
    widget.setStyleClass(getStyleClass());
    return widget;
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return null;
  }
}
