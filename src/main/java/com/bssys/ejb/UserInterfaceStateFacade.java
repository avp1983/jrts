package com.bssys.ejb;

import com.bssys.entity.UserInterfaceState;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.math.BigDecimal;

@Named
@Stateless
public class UserInterfaceStateFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  public DashboardState getTaskBarStateFromDB(int userKey) {
    try {
      UserInterfaceState state = em.find(UserInterfaceState.class, userKey);
      return state.getTaskBar();
    } catch (Exception ex) {
      return null;
    }
  }

  public DashboardState getMainPageDashboardFromDB(int userKey) {
    try {
      UserInterfaceState state = em.find(UserInterfaceState.class, userKey);
      return state.getMainPageDashboard();
    } catch (Exception ex) {
      return null;
    }
  }

  public void saveTaskBarToDB(int userKey, DashboardState dashboardState) {
    UserInterfaceState userInterfaceState = em.find(UserInterfaceState.class, userKey);
    userInterfaceState.setTaskBar(dashboardState);
    em.merge(userInterfaceState);
    em.flush();
  }

  public void saveMainPageDashboardToDB(int userKey, DashboardState dashboardState) {
    UserInterfaceState userInterfaceState = em.find(UserInterfaceState.class, userKey);
    userInterfaceState.setMainPageDashboard(dashboardState);
    em.merge(userInterfaceState);
    em.flush();
  }

  public boolean isNewJrtsUser(int userKey) {
    BigDecimal res = (BigDecimal) em.createNativeQuery(
        "select cast(count(*) as decimal) from RemoteUser where UserKey = ? " +
            " and taskbar is null and mainpagedashboard is null and lastrequest is null")
        .setParameter(1, userKey).getSingleResult();
    return (res.intValue() != 0);
  }

}
