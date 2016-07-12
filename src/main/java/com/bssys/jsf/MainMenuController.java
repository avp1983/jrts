package com.bssys.jsf;

import com.bssys.ejb.MainMenuFacade;
import com.bssys.ejb.RemotePassCfgFacade;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.ejb.UmtTransferSysDataFacade;
import com.bssys.ejb.UserInterfaceStateFacade;
import com.bssys.entity.RemotePassCfg;
import com.bssys.session.UserSessionBean;
import org.primefaces.model.menu.MenuModel;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.EnumSet;

@Named("mainMenuController")
@ViewScoped
public class MainMenuController implements Serializable {
  private static final long serialVersionUID = 1L;
  @Inject
  private MainMenuFacade mainMenuFacade;

  @Inject
  private UserInterfaceStateFacade userInterfaceStateFacade;

  @Inject
  private RemotePassCfgFacade remotePassCfgFacade;

  @Inject
  private UmtTransferSysDataFacade transferSysDataFacade;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private UmtAgentFacade agentFacade;

  private MenuModel menuModel;

  public MenuModel getMenuModel() {
    if (menuModel == null) {
      menuModel = createMenuModel(mainMenuFacade.getMainMenuItems(userSession.getUserKey()));
    }
    return menuModel;
  }

  private MenuModel createMenuModel(final EnumSet<MainMenuItemEnum> menuItems) {
    MainMenuConstructor menuConstructor = new MainMenuConstructor(menuItems, userSession, agentFacade);
    menuConstructor.createAdminSubMenu();
    menuConstructor.createTransferSubMenu();
    menuConstructor.createMessageSubMenu();
    menuConstructor.createReportSubMenu();
    menuConstructor.createDictionarySubMenu();
    menuConstructor.createServiceSubMenu();
    return menuConstructor.getMenuModel();
  }

  public boolean isShowMenuTutorial() {
    return userInterfaceStateFacade.isNewJrtsUser(userSession.getUserKey());
  }

  public RemotePassCfg getRemotePassConfig() {
    return remotePassCfgFacade.getRemotePassConfig();
  }

  public String getNativeTransferL18nName() {
    return transferSysDataFacade.getNativeTransferL18nName(userSession.getUserLocale());
  }
}
