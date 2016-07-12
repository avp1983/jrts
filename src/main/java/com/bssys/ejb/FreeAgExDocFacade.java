package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.entity.FreeAgExDoc;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Named("FreeAgExDocFacade")
@Stateless
public class FreeAgExDocFacade implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  public Integer getStatusByIDRForUser(DboDocPK dboDocPK) {
    TypedQuery<BigDecimal> query = em.createNamedQuery("FreeAgExDoc.getStatusByPK", BigDecimal.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<BigDecimal> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }
    return resList.get(0).intValue();
  }

  public String getDocumentNumberByIDR(DboDocPK dboDocPK) {
    TypedQuery<String> query = em.createNamedQuery("FreeAgExDoc.getDocumentNumberByPK", String.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<String> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }
    return resList.get(0);
  }

  public Date getDocumentDateByIDR(DboDocPK dboDocPK) {
    TypedQuery<Date> query = em.createNamedQuery("FreeAgExDoc.getDocumentDateByPK", Date.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<Date> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }
    return resList.get(0);
  }

  public String getFreeDocTypeDescrByIDR(DboDocPK dboDocPK) {
    TypedQuery<String> query = em.createNamedQuery("FreeAgExDoc.getDocumentTypeStrByPK", String.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<String> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }
    return resList.get(0);
  }

  public FreeAgExDoc getDocumentByID(DboDocPK dboDocPK) {
    return em.find(FreeAgExDoc.class, dboDocPK);
  }
}
