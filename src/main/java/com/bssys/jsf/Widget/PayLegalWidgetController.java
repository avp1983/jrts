package com.bssys.jsf.Widget;

import com.bssys.bls.types.DboDocPK;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UmtPayLegalFacade;
import com.bssys.entity.PayLegal.UmtPayLegal;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultController;
import com.bssys.rts.RtsConnectConfigurator;
import com.bssys.session.UserSessionBean;
import com.google.common.collect.Sets;
import org.primefaces.component.menubutton.MenuButton;
import org.primefaces.component.panel.Panel;
import org.primefaces.context.RequestContext;
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

import static com.bssys.jsf.Widget.DashboardWidgetInfo.PAY_LEGAL;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;
import static com.bssys.umt.Statuses.STS_BSI_FOO;
import static com.bssys.umt.Statuses.STS_BSI_NEW;
import static com.bssys.umt.Statuses.STS_NEW;
import static com.bssys.umt.Statuses.STS_PAYLEGAL_READY_TOPAY;

@Named(PAY_LEGAL)
@ViewScoped
public class PayLegalWidgetController implements DashboardWidgetController, Serializable {

  @Inject
  private UmtPayLegalFacade umtPayLegalFacade;
  @Inject
  private UmtAgentFacade umtAgentFacade;
  @Inject
  private UserSessionBean userSession;
  @Inject
  private ProcessStepResultController resultController;
  @Inject
  private RtsConnectConfigurator rtsConnectConfigurator;

  private static final Set<Integer> STATUSES_FOR_CONTINUE_ACTION =
      Sets.newHashSet(STS_BSI_FOO, STS_BSI_NEW, STS_NEW, STS_PAYLEGAL_READY_TOPAY);

  private void addActionsMenuItem(DefaultMenuModel menuModel, String actionLabel, String jsAction, boolean isUpdateForm) {
    DefaultMenuItem menuItemView =
        new DefaultMenuItem(userSession.getLocRes("umtpaylegal", actionLabel));
    menuItemView.setOnclick(jsAction);
    if (isUpdateForm) {
      menuItemView.setUpdate("@(form)");
    }
    menuModel.addElement(menuItemView);
  }

  private boolean isTransferHasContinueAction(UmtPayLegal doc) {
    return STATUSES_FOR_CONTINUE_ACTION.contains(doc.getStatus());
  }

  @Override
  public Panel createWidget(Object widgetData) {
    Panel widget = new Panel();
    String docIDR = (String) widgetData;

    widget.setHeader(userSession.getLocRes("umtpaylegal", "PAYLEGAL_TRANSFER"));
    FacesContext context = FacesContext.getCurrentInstance();
    ViewDeclarationLanguage vdl = context.getApplication().getViewHandler()
        .getViewDeclarationLanguage(context, context.getViewRoot().getViewId());
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("widgetData", docIDR);
    UIComponent component =
        vdl.createComponent(context, "http://java.sun.com/jsf/composite/widget", "paylegal", hashMap);
    widget.getChildren().add(component);

    MenuButton menuButton = new MenuButton();
    menuButton.setValue("");
    menuButton.setStyleClass("ui-panel-titlebar-icon ui-panel-titlebar-icon-left ui-panel-titlebar-icon-options");
    DefaultMenuModel menuModel = new DefaultMenuModel();
    menuButton.setModel(menuModel);

    UmtPayLegal doc = umtPayLegalFacade.getDocumentByID(new DboDocPK(docIDR));
    if (isTransferHasContinueAction(doc)) {
      addActionsMenuItem(menuModel, "PAYLEGAL_ACTION_CONTINUE",
          String.format("openSupposedPayLegalActionForm('%s')", docIDR), true);
    }

    addActionsMenuItem(menuModel, "PAYLEGAL_ACTION_VIEW",
        String.format("navigateView('UMTPAYLEGAL', 'VIEW', '%s', null, 'paylegalwidget', 1070, 650)", docIDR),
        false);


    addActionsMenuItem(menuModel, "PAYLEGAL_ACTION_COPY", String.format("copyPayLegal([{name: 'IDR', value: '%s'}])", docIDR), false);
    widget.getFacets().put("actions", menuButton);
    widget.setStyleClass("paytransferopen-widget");
    return widget;
  }


  public void copyTransfer() {
    String idr = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
    DboDocPK newPK = umtPayLegalFacade.copy(new DboDocPK(idr));
    RequestContext context = RequestContext.getCurrentInstance();
    context.addCallbackParam("scheme", "UMTPAYLEGAL");
    context.addCallbackParam("newpk", newPK.toDelimString());

    resultController.processResult(new ProcessStepResult(userSession.getLocRes("umtpaylegal", "PAYLEGAL_SUCCESS_COPY"), PROCESS_RESULT_OK));
  }

  @Override
  public Object getWidgetDataFromRequest() {
    return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("IDR");
  }
}
