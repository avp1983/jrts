package com.bssys.jsf.Widget;

import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.session.UserSessionBean;
import org.primefaces.component.panel.Panel;

import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.WEB_PROVIDER_CATEGORY_WIDGET;

@Named(WEB_PROVIDER_CATEGORY_WIDGET)
@ViewScoped
public class WebProviderCategoryWidgetController implements DashboardWidgetController, Serializable {

  @Inject
  private UserSessionBean userSession;

  @Inject
  private UMTWebProviderFacade webProviderFacade;

  @Override
  public Panel createWidget(Object widgetData) {
    int categoryID = Integer.parseInt((String) widgetData);

    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", categoryID);
    return (Panel) vdl.createComponent(context,
        "http://java.sun.com/jsf/composite/widget", "webprovidercategory", hashMap);
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get("CATEGORY");
  }

}
