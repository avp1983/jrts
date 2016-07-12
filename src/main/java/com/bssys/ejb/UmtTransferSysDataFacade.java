package com.bssys.ejb;

import com.bssys.entity.UmtTransferSysData;

import javax.ejb.Singleton;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Locale;

@Singleton
@Named
public class UmtTransferSysDataFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  private UmtTransferSysData nativeTransferSysData;

  public UmtTransferSysData getNativeTransferSysData() {
    if (nativeTransferSysData == null) {
      nativeTransferSysData = em.find(UmtTransferSysData.class, 0L);
    }
    return nativeTransferSysData;
  }

  public String getNativeTransferL18nName(final Locale locale) {
    return locale.equals(new Locale("ru", "RU")) ?
        getNativeTransferSysData().getName() : getNativeTransferSysData().getNameEng();
  }
}