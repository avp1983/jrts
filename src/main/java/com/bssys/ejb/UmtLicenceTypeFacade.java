package com.bssys.ejb;

import org.apache.commons.lang.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;

import static com.bssys.umt.TransferSys.TRANSFER_SYS_NATIVE;

@Stateless
@Named
public class UmtLicenceTypeFacade implements Serializable {
  @PersistenceContext
  private transient EntityManager em;

  public List<String> getNameLike(String name, int transferSys) {
    return em.createNamedQuery("UMTLicenceType.getNameLike", String.class)
        .setParameter(1, StringUtils.upperCase(name) + "%")
        .setParameter(2, transferSys)
        .setParameter(3, TRANSFER_SYS_NATIVE)
        .getResultList();
  }

}