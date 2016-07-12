package com.bssys.jsf.Widget;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UmtPayTransferCloseFacade;
import com.bssys.entity.UmtPayTransferClose;
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

import static com.bssys.jsf.Widget.DashboardWidgetInfo.PAY_TRANSFER_CLOSE;
import static com.bssys.umt.Statuses.STS_BSI_FOO;
import static com.bssys.umt.Statuses.STS_BSI_NEW;
import static com.bssys.umt.Statuses.STS_NEW;
import static com.bssys.umt.Statuses.STS_READY_TOPAY;

@Named(PAY_TRANSFER_CLOSE)
@ViewScoped
public class PayTransferCloseWidgetController implements DashboardWidgetController, Serializable {

  @Inject
  private UmtPayTransferCloseFacade umtPayTransferCloseFacade;
  @Inject
  private UmtAgentFacade umtAgentFacade;

  @Inject
  private UserSessionBean userSession;

  private static final Set<Integer> STATUSES_FOR_CONTINUE_ACTION =
      Sets.newHashSet(STS_BSI_FOO, STS_NEW, STS_BSI_NEW, STS_READY_TOPAY);

  private boolean isTransferHasContinueAction(UmtPayTransferClose doc) {
    return (STATUSES_FOR_CONTINUE_ACTION.contains(doc.getStatus()));
  }

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    String docIDR = (String) widgetData;

    widget.setHeader(userSession.getLocRes("paytransferclose", "PAY_TRANSFER_CLOSE_TRANSFER"));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", docIDR);
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "paytransferclose", hashMap);
    widget.getChildren().add(component);

    MenuButton menuButton = new MenuButton();
    menuButton.setValue("");
    menuButton.setStyleClass("ui-panel-titlebar-icon ui-panel-titlebar-icon-left ui-panel-titlebar-icon-options");
    DefaultMenuModel menuModel = new DefaultMenuModel();
    menuButton.setModel(menuModel);

    UmtPayTransferClose doc = umtPayTransferCloseFacade.getDocumentByID(new DboDocPK(docIDR));
    addActionsMenuItem(menuModel, "PAY_TRANSFER_CLOSE_ACTION_VIEW",
        String.format("navigateView('UMTPAYTRANSFERCLOSE', 'VIEW', '%s', null, 'paytransferclosewidget')", docIDR), false);

    if (isTransferHasContinueAction(doc)) {
      addActionsMenuItem(menuModel, "PAY_TRANSFER_CLOSE_ACTION_CONTINUE",
          String.format("openSupposedPayoutActionForm('%s')", docIDR), true);
    }

    widget.getFacets().put("actions", menuButton);
    widget.setStyleClass("paytransferopen-widget");
    return widget;
  }

  private void addActionsMenuItem(DefaultMenuModel menuModel, String actionLabel, String jsAction, boolean isUpdateForm) {
    DefaultMenuItem menuItemView = new DefaultMenuItem(userSession.getLocRes("paytransferclose", actionLabel));
    menuItemView.setOnclick(jsAction);
    if (isUpdateForm) {
      menuItemView.setUpdate("@(form)");
    }
    menuModel.addElement(menuItemView);
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
  }
}
