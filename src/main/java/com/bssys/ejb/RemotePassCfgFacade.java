package com.bssys.ejb;

import com.bssys.entity.RemotePassCfg;

import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Singleton
@Named
public class RemotePassCfgFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  private RemotePassCfg remotePassCfg;

  public RemotePassCfg getRemotePassConfig() {
    if (remotePassCfg == null) {
      remotePassCfg = em.find(RemotePassCfg.class, 1L);
    }
    return remotePassCfg;
  }
}
