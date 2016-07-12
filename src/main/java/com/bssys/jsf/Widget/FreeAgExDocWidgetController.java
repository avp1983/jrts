package com.bssys.jsf.Widget;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.FreeAgExDocFacade;
import com.bssys.entity.FreeAgExDoc;
import com.bssys.session.UserSessionBean;
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

import static com.bssys.jsf.Widget.DashboardWidgetInfo.FREE_AG_EX_DOC;
import static com.bssys.umt.Statuses.STS_BSI_FOO;

@Named(FREE_AG_EX_DOC)
@ViewScoped
public class FreeAgExDocWidgetController implements DashboardWidgetController, Serializable {

  @Inject
  private UserSessionBean userSession;

  @Inject
  private FreeAgExDocFacade freeAgExDocFacade;

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    String docIDR = (String) widgetData;

    widget.setHeader(userSession.getLocRes("freeagexdoc", "FREE_AG_EX_DOC"));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", docIDR);
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "freeagexdoc", hashMap);
    widget.getChildren().add(component);

    MenuButton menuButton = new MenuButton();
    menuButton.setValue("");
    menuButton.setStyleClass("ui-panel-titlebar-icon ui-panel-titlebar-icon-left ui-panel-titlebar-icon-options");
    DefaultMenuModel menuModel = new DefaultMenuModel();
    menuButton.setModel(menuModel);

    FreeAgExDoc doc = freeAgExDocFacade.getDocumentByID(new DboDocPK(docIDR));

    if (isFreeAgExDocHasContinueAction(doc)) {
      addActionsMenuItem(menuModel, "FREE_AG_EX_DOC_ACTION_CONTINUE",
          String.format("navigateForm('FREEAGEXDOC', 'EDIT', '%s', null, 'freeagexdocwidget');", docIDR), true);
    }
    addActionsMenuItem(menuModel, "FREE_AG_EX_DOC_ACTION_VIEW",
        String.format("navigateView('FREEAGEXDOC', 'VIEW', '%s', null, 'freeagexdocwidget')", docIDR), false);

    widget.getFacets().put("actions", menuButton);
    widget.setStyleClass("paytransferopen-widget");
    return widget;
  }

  private boolean isFreeAgExDocHasContinueAction(FreeAgExDoc doc) {
    return (STS_BSI_FOO == doc.getStatus() && userSession.getAgentId() == doc.getDboDocPK().getClient());
  }

  private void addActionsMenuItem(DefaultMenuModel menuModel, String actionLabel, String jsAction, boolean isUpdateForm) {
    DefaultMenuItem menuItemView =
        new DefaultMenuItem(userSession.getLocRes("freeagexdoc", actionLabel));
    menuItemView.setOnclick(jsAction);
    if (isUpdateForm) {
      menuItemView.setUpdate("@(form)");
    }
    menuModel.addElement(menuItemView);
  }

  @Override
  public Object getWidgetDataFromRequest() {
    // TODO: check idr exists
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
  }
}
