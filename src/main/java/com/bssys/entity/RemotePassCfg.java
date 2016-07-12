package com.bssys.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "REMOTEPASSCFG")
public class RemotePassCfg implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  private Long ID;

  private int clientAutoLogoutTimeout;

  public int getClientAutoLogoutTimeout() {
    return clientAutoLogoutTimeout;
  }
}
