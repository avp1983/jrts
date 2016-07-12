package com.bssys.jsf;

import com.bssys.ejb.AgentAllowActions;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.session.UserSessionBean;
import com.google.common.collect.Sets;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.model.menu.DefaultMenuColumn;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuColumn;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.model.menu.Submenu;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashSet;

import static com.bssys.ejb.AgentAllowActions.CAN_PAY_TRANSFERS;
import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS;
import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS_LEGAL_ENTITIES;
import static com.bssys.jsf.MainMenuItemEnum.*;

public class MainMenuConstructor {
  private final DefaultMenuModel menuModel;
  private final UserSessionBean userSession;

  private final DefaultSubMenu rootSubmenu;
  private final EnumSet<MainMenuItemEnum> menuItems;
  private final UmtAgentFacade agentFacade;
  private final EnumSet<AgentAllowActions> agentAllowActions;

  public MainMenuConstructor(final EnumSet<MainMenuItemEnum> menuItems, final UserSessionBean userSessionController,
                             UmtAgentFacade agentFacade) {
    this.menuItems = menuItems;
    this.userSession = userSessionController;
    this.menuModel = new DefaultMenuModel();
    this.rootSubmenu = createRootSubMenu(menuModel);
    this.agentFacade = agentFacade;
    agentAllowActions = agentFacade.getAgentAllowActions(userSession.getAgentId());
  }

  public DefaultMenuModel getMenuModel() {
    return menuModel;
  }

  DefaultSubMenu createRootSubMenu(final MenuModel menuModel) {
    DefaultSubMenu rootSubmenu = new DefaultSubMenu();
    menuModel.addElement(rootSubmenu);
    rootSubmenu.setIcon("bssmainmenuicon");
    return rootSubmenu;
  }

  @SuppressWarnings("unchecked")
  void addSubMenu(final Submenu submenu) {
    if (submenu.getElementsCount() > 0) {
      MenuColumn menuColumn = new DefaultMenuColumn();
      menuColumn.getElements().add(submenu);
      rootSubmenu.addElement(menuColumn);
    }
  }

  private void addMenuItem(final DefaultSubMenu subMenu, final String itemLabel, final String onClickAction) {
    DefaultMenuItem menuItem = new DefaultMenuItem();
    menuItem.setValue(userSession.getLocRes("mainmenu", itemLabel));
    menuItem.setStyleClass("ui-mainmenu-menuitem-" + itemLabel);
    menuItem.setOnclick(onClickAction + " return;");
    subMenu.addElement(menuItem);
  }

  private void processMenuItem(final DefaultSubMenu subMenu, final MainMenuItemEnum itemName, final String itemLabel,
                               final String onClickAction) {
    if (menuItems.contains(itemName)) {
      addMenuItem(subMenu, itemLabel, onClickAction);
    }
  }

  void createTransferSubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "TRANSFERS"));
    if (agentAllowActions.contains(CAN_SEND_TRANSFERS)) {
      processMenuItem(subMenu, TRANSFER_NEW, "TRANSFER_NEW",
          "navigateScrollFilter('UMTPAYTRANSFEROPEN_CREATE', null, 'mainmenu');");
    }
    if (agentAllowActions.contains(CAN_PAY_TRANSFERS)) {
      processMenuItem(subMenu, TRANSFER_PAY, "TRANSFER_PAY",
          "navigateBSI('RT_2UMTPAYTRANSFERCLOSE.setPropsSearch','UMTPAYTRANSFEROPEN', 'PAY', null, null, null, 'mainmenu');");
    }
    processMenuItem(subMenu, TRANSFER_LIST, "TRANSFER_LIST",
        "navigateScrollFilter('UMTPAYTRANSFEROPEN', null, 'mainmenu');");
    if (StringUtils.equalsIgnoreCase("russian", userSession.getLanguageID()) &&
        (agentAllowActions.contains(CAN_SEND_TRANSFERS_LEGAL_ENTITIES)) &&
        (menuItems.contains(TRANSFER_NEW) || menuItems.contains(TRANSFER_PAY) || menuItems.contains(TRANSFER_LIST))) {
      addMenuItem(subMenu, "PAYLEGAL_LIST", "navigateScrollFilter('UMTPAYLEGAL', null, 'mainmenu');");
    }
    addSubMenu(subMenu);
  }

  void createMessageSubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "MESSAGES"));
    processMenuItem(subMenu, MESSAGE_ADMIN, "MESSAGE_ADMIN",
        "navigateScrollFilter('FREECLIENTDOC', null, 'mainmenu');");
    processMenuItem(subMenu, MESSAGE_AGENT, "MESSAGE_AGENT",
        "navigateScrollFilter('FREEAGEXDOC', null, 'mainmenu');");
    processMenuItem(subMenu, MESSAGE_INBOX, "MESSAGE_FROM_ADMIN",
        "if (mw.resetNewMsgFromAdmin) { mw.resetNewMsgFromAdmin(); } navigateScrollFilter('FREECLIENTDOC_INBOX', null, 'mainmenu');");
    processMenuItem(subMenu, MESSAGE_AGENT_INBOX, "MESSAGE_FROM_AGENT",
        "if (mw.resetNewMsgFromAgents) { mw.resetNewMsgFromAgents(); } navigateScrollFilter('FREEAGEXDOC_INBOX', null, 'mainmenu');");
    addMenuItem(subMenu, "MESSAGE_NEWS",
        "if (mw.resetNewsAddedToday) { mw.resetNewsAddedToday(); } navigateScroller('NEWS','DEFAULT','mainmenu');");
    addSubMenu(subMenu);
  }

  void createReportSubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "REPORT"));
    if (!Sets.intersection(menuItems,
        new HashSet<>(Arrays.asList(REPORT_OPERATION, REPORT_FINANCE, REPORT_MUTUAL, REPORT_READY))).isEmpty()) {
      addMenuItem(subMenu, "REPORT_READY", "if (mw.resetFinishedReports) { mw.resetFinishedReports();} navigateScrollFilter('ExprRepRequest',null,'mainmenu');");
    }
    processMenuItem(subMenu, REPORT_OPERATION, "REPORT_OPERATION", "navigateReport('1');");
    processMenuItem(subMenu, REPORT_FINANCE, "REPORT_FINANCE", "navigateReport('2');");
    processMenuItem(subMenu, REPORT_MUTUAL, "REPORT_MUTUAL", "navigateReport('Mutual');");
    processMenuItem(subMenu, REPORT_PSPAYMENTS, "REPORT_PSPAYMENTS",
        "navigateScroller('UMTORDERTOUNLOAD', 'DEFAULT', 'mainmenu');");
    addSubMenu(subMenu);
  }

  void createDictionarySubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "DICTIONARY"));
    processMenuItem(subMenu, DICTIONARY_PROPS, "DICTIONARY_PROPS",
        "navigateScrollFilter('Requisites', null, 'mainmenu');");
    processMenuItem(subMenu, DICTIONARY_CLIENTS, "DICTIONARY_CLIENTS",
        "navigateScroller('UMTTransferClient','DEFAULT','mainmenu');");
    processMenuItem(subMenu, DICTIONARY_LAW, "DICTIONARY_LAW",
        "navigateScroller('UMTLegislationInfo','DEFAULT','mainmenu');");
    processMenuItem(subMenu, DICTIONARY_DEST_POINTS, "DICTIONARY_DEST_POINTS",
        "navigateScrollFilter('DestPoints', null, 'mainmenu');");
    processMenuItem(subMenu, DICTIONARY_APERS, "DICTIONARY_APERS",
        "navigateScroller('REMOTEOFFICIALS','DEFAULT','mainmenu');");
    processMenuItem(subMenu, DICTIONARY_STOPLIST, "DICTIONARY_STOPLIST",
        "navigateScroller('BLOCKLIST','DEFAULT','mainmenu');");
    addSubMenu(subMenu);
  }

  void createServiceSubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "SERVICE"));
    processMenuItem(subMenu, SERVICE_ABS, "SERVICE_ABS", "navigateScrollFilter('ABSSETTINGS', null, 'mainmenu');");
    processMenuItem(subMenu, SERVICE_CRYPTO, "SERVICE_CRYPTO", "navigateScrollFilter('CRYPTO', null, 'mainmenu');");
    processMenuItem(subMenu, SERVICE_PASSWORD, "SERVICE_PASSWORD", "navigateBSI('RT_1ENTRY.CPF', null, null, null, null, null, 'mainmenu');");
    processMenuItem(subMenu, SERVICE_USERMANUAL, "SERVICE_USERMANUAL", "navigateBSI('RT_2IC.FRAME_MANUAL', null, null, null, null, null, 'mainmenu');");
    addSubMenu(subMenu);
  }

  void createAdminSubMenu() {
    DefaultSubMenu subMenu = new DefaultSubMenu(userSession.getLocRes("mainmenu", "ADMIN"));
    if (menuItems.contains(ADMIN_AGENTS)) {
      processMenuItem(subMenu, ADMIN_AGENTS_ACTIVE, "ADMIN_AGENTS_ACTIVE",
          "navigateBSI('RT_2APAgents.GetActiveAgentsTree', 'APAGENTS', 'VIEW_AGENTSTREE', null, null, null, 'mainmenu');");
      processMenuItem(subMenu, ADMIN_AGENTS_DELETED, "ADMIN_AGENTS_DELETED",
          "navigateScroller('CUSTOMER', 'LOCKEDAGENTS', 'mainmenu');");
    }
    processMenuItem(subMenu, ADMIN_JOURNALS, "ADMIN_JOURNALS",
        "navigateScrollFilter('ADMINJOURNAL', null, 'mainmenu');");
    processMenuItem(subMenu, ADMIN_CERTIFICATE_REQUEST, "ADMIN_CERTIFICATE_REQUEST",
        "if (mw.resetNewCertRequest) { mw.resetNewCertRequest();} navigateScrollFilter('ADMINCRYPTO', null, 'mainmenu');");
    if (agentFacade.isRootAgent(userSession.getAgentId())) {
      processMenuItem(subMenu, ADMIN_ADDITIONAL_COMMISSIONS, "ADMIN_ADDITIONAL_COMMISSIONS",
          "if (mw.resetAddCommissions) { mw.resetAddCommissions();} navigateScroller('UMTCommExt','DEFAULT'," +
              "'mainmenu');"
      );
    }
    addSubMenu(subMenu);
  }
}
