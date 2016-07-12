package com.bssys.jsf.Widget;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.FreeClientDocFacade;
import com.bssys.entity.FreeClientDoc;
import com.bssys.session.UserSessionBean;
import com.google.common.collect.Sets;
import org.primefaces.component.menubutton.MenuButton;
import org.primefaces.component.panel.Panel;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Set;

import static com.bssys.jsf.Widget.DashboardWidgetInfo.FREE_CLIENT_DOC;
import static com.bssys.umt.Statuses.STS_BSI_FOO;
import static com.bssys.umt.Statuses.STS_BSI_NEW;

@Named(FREE_CLIENT_DOC)
@ViewScoped
public class FreeClientDocWidgetController implements DashboardWidgetController, Serializable {
  private static final Set<Integer> STATUSES_FOR_CONTINUE_ACTION = Sets.newHashSet(STS_BSI_FOO, STS_BSI_NEW);
  @Inject
  private UserSessionBean userSession;

  @Inject
  private FreeClientDocFacade freeClientDocFacade;

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    String docIDR = (String) widgetData;

    widget.setHeader(userSession.getLocRes("freeclientdoc", "FREE_CLIENT_DOC"));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", docIDR);
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "freeclientdoc", hashMap);
    widget.getChildren().add(component);

    MenuButton menuButton = new MenuButton();
    menuButton.setValue("");
    menuButton.setStyleClass("ui-panel-titlebar-icon ui-panel-titlebar-icon-left ui-panel-titlebar-icon-options");
    DefaultMenuModel menuModel = new DefaultMenuModel();
    menuButton.setModel(menuModel);

    FreeClientDoc doc = freeClientDocFacade.getDocumentByID(new DboDocPK(docIDR));
    if (isFreeClientDocHasContinueAction(doc)) {
      addActionsMenuItem(menuModel, "FREE_CLIENT_DOC_ACTION_CONTINUE",
          String.format("navigateForm('FREECLIENTDOC', 'EDIT', '%s', null, 'freeclientdocwidget');", docIDR), true);
    }
    addActionsMenuItem(menuModel, "FREE_CLIENT_DOC_ACTION_VIEW",
        String.format("navigateView('FREECLIENTDOC', 'VIEW', '%s', null, 'freeclientdocwidget')", docIDR), false);

    widget.getFacets().put("actions", menuButton);
    widget.setStyleClass("paytransferopen-widget");
    return widget;
  }

  private boolean isFreeClientDocHasContinueAction(FreeClientDoc doc) {
    return (STATUSES_FOR_CONTINUE_ACTION.contains(doc.getStatus()) &&
        userSession.getAgentId() == doc.getDboDocPK().getClient());
  }

  private void addActionsMenuItem(DefaultMenuModel menuModel, String actionLabel, String jsAction, boolean isUpdateForm) {
    DefaultMenuItem menuItemView =
        new DefaultMenuItem(userSession.getLocRes("freeclientdoc", actionLabel));
    menuItemView.setOnclick(jsAction);
    if (isUpdateForm) {
      menuItemView.setUpdate("@(form)");
    }
    menuModel.addElement(menuItemView);
  }

  @Override
  public Object getWidgetDataFromRequest() {
    // TODO: Check if idr exists
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
  }
}
