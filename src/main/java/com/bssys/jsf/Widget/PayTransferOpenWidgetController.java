package com.bssys.jsf.Widget;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UmtPayTransferOpenFacade;
import com.bssys.entity.UmtPayTransferOpen;
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

import static com.bssys.jsf.Widget.DashboardWidgetInfo.PAY_TRANSFER_OPEN;
import static com.bssys.umt.Statuses.*;

@Named(PAY_TRANSFER_OPEN)
@ViewScoped
public class PayTransferOpenWidgetController implements DashboardWidgetController, Serializable {

  @Inject
  private UmtPayTransferOpenFacade transferOpenFacade;
  @Inject
  private UmtAgentFacade umtAgentFacade;
  @Inject
  private UserSessionBean userSession;

  private static final Set<Integer> STATUSES_FOR_RETURN = Sets.newHashSet(STS_TOPAY, STS_UMT_CANCELED);

  private static final Set<Integer> STATUSES_FOR_UNBLOCK =
      Sets.newHashSet(STS_BLOCKEDPAID, STS_BLOCKEDCHANGE, STS_BLOCKED_RETURN);

  private static final Set<Integer> STATUSES_FOR_CONTINUE_ACTION =
      Sets.newHashSet(STS_BSI_FOO, STS_PKO_REQUEST, STS_BSI_NEW, STS_NEW, STS_READY_TOSEND);

  private void addActionsMenuItem(DefaultMenuModel menuModel, String actionLabel, String jsAction, boolean isUpdateForm) {
    DefaultMenuItem menuItemView =
        new DefaultMenuItem(userSession.getLocRes("payTransferOpen", actionLabel));
    menuItemView.setOnclick(jsAction);
    if (isUpdateForm) {
      menuItemView.setUpdate("@(form)");
    }
    menuModel.addElement(menuItemView);
  }

  private boolean isTransferCanBeUnblocked(UmtPayTransferOpen doc) {
    return (STATUSES_FOR_UNBLOCK.contains(doc.getStatus()) &&
        (userSession.getAgentId() == doc.getDestclient()));
  }

  private boolean isTransferHasContinueAction(UmtPayTransferOpen doc) {
    return (STATUSES_FOR_CONTINUE_ACTION.contains(doc.getStatus()) ||
        isTransferCanBeUnblocked(doc));
  }

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    String docIDR = (String) widgetData;

    widget.setHeader(userSession.getLocRes("payTransferOpen", "PAY_TRANSFER_OPEN_TRANSFER"));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", docIDR);
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "paytransferopen", hashMap);
    widget.getChildren().add(component);

    MenuButton menuButton = new MenuButton();
    menuButton.setValue("");
    menuButton.setStyleClass("ui-panel-titlebar-icon ui-panel-titlebar-icon-left ui-panel-titlebar-icon-options");
    DefaultMenuModel menuModel = new DefaultMenuModel();
    menuButton.setModel(menuModel);

    UmtPayTransferOpen doc = transferOpenFacade.getDocumentByID(new DboDocPK(docIDR));
    if (isTransferHasContinueAction(doc)) {
      addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_CONTINUE",
          String.format("openSupposedTransferActionForm('%s')", docIDR), true);
    }
    if (isTransferCanBeUnblocked(doc)) {
      addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_UNBLOCK",
          String.format("unblockTransfer('%s')", docIDR), true);
    }
    if (doc.getStatus() == STS_TOPAY && doc.getCustid() == userSession.getAgentId()) {
      addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_UNDO_SENDING",
          String.format("undoTransferSending('%s')", docIDR), true);

      addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_EDIT",
          String.format("editTransfer('%s')", docIDR), true);
    }

    if (STATUSES_FOR_RETURN.contains(doc.getStatus()) &&
        doc.getCustid() == userSession.getAgentId()) {
      addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_RETURN",
          String.format("returnTransfer('%s')", docIDR), true);
    }

    addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_VIEW",
        String.format("navigateView('UMTPAYTRANSFEROPEN', 'VIEW', '%s', null, 'paytransferopenwidget')", docIDR), false);

    addActionsMenuItem(menuModel, "PAY_TRANSFER_OPEN_ACTION_COPY",
        String.format("copyTransfer('%s')", docIDR), false);

    widget.getFacets().put("actions", menuButton);
    widget.setStyleClass("paytransferopen-widget");
    return widget;
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
  }

  public String getICStatusDescriptionByStatus(int status) {
    return transferOpenFacade.getICStatusDescriptionByStatus(status, userSession.getUserLocale());
  }
}
