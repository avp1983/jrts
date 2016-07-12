package com.bssys.ejb;

import com.bssys.jsf.MainMenuItemEnum;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.EnumSet;

@Stateless
@Named
public class MainMenuFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  @SuppressWarnings("unchecked")
  public EnumSet<MainMenuItemEnum> getMainMenuItems(int userKey) {
    Query query = em.createNativeQuery(
        "select MENU_ID "
            + "from "
            + "("
            + "select MENU_ID "
            + "from RemoteMenu rm "
            + "inner join RemoteMenuItemActions rmactions on rm.Id=rmactions.ItemID "
            + "inner join UMTICUserRights rights on rmactions.ActionID=rights.ActionID "
            + "inner join RemoteUserRoles roles on rights.RoleID=roles.RoleID and roles.ICUserID=?1 "
            + "union "
            + "select MENU_ID "
            + "from RemoteMenu common_rm "
            + "where not exists (select 1 from RemoteMenuItemActions rmactions where common_rm.Id=rmactions.ItemID)"
            + ") menu "
    )
        .setParameter(1, userKey);

    EnumSet<MainMenuItemEnum> mainMenuItemSet = EnumSet.noneOf(MainMenuItemEnum.class);
    for (Object item : query.getResultList()) {
      if (item != null) {
        mainMenuItemSet.add(MainMenuItemEnum.valueOf((String) item));
      }
    }
    return mainMenuItemSet;
  }
}
