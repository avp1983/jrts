package com.bssys.jsf.Widget;

import com.bssys.ejb.UMTWebProviderFacade;
import com.bssys.entity.UMTWebProviderHelper;
import com.bssys.session.UserSessionBean;
import org.primefaces.component.panel.Panel;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.FAVOURITE_PROVIDERS_WIDGET;

@Named(FAVOURITE_PROVIDERS_WIDGET)
@ViewScoped
public class FavouritesWidgetController implements DashboardWidgetController, Serializable {
  private List<UMTWebProviderHelper> favourites;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private UMTWebProviderFacade webProviderFacade;

  @Override
  public Panel createWidget(Object widgetData) {
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", "");
    return (Panel) vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "favourites", hashMap);
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return "";
  }

  @PostConstruct
  private void initBean(){
    favourites = webProviderFacade.getFavourites();
  }

  public List<UMTWebProviderHelper> getFavourites() {
    return favourites;
  }
}
