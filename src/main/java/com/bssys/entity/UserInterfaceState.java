package com.bssys.entity;

import com.bssys.ejb.DashboardState;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REMOTEUSER")
public class UserInterfaceState implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private int userKey;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private DashboardState taskBar;

  @Lob
  @Basic(fetch = FetchType.LAZY)
  private DashboardState mainPageDashboard;

  public int getUserKey() {
    return userKey;
  }

  public void setUserKey(int userKey) {
    this.userKey = userKey;
  }

  public DashboardState getMainPageDashboard() {
    try {
      return mainPageDashboard;
    } catch (Exception ex) {
      return null;
    }
  }

  public void setMainPageDashboard(DashboardState mainPageDashboard) {
    this.mainPageDashboard = mainPageDashboard;
  }

  public DashboardState getTaskBar() {
    try {
      return taskBar;
    } catch (Exception ex) {
      return null;
    }
  }

  public void setTaskBar(DashboardState taskBarModel) {
    this.taskBar = taskBarModel;
  }
}
