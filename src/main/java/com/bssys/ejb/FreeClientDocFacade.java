package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.entity.FreeClientDoc;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Named("FreeClientDocFacade")
@Stateless
public class FreeClientDocFacade implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  public void create(final FreeClientDoc entity) {
    em.persist(entity);
  }

  public Integer getStatusByIDRForUser(DboDocPK dboDocPK) {
    TypedQuery<BigDecimal> query = em.createNamedQuery("FreeClientDoc.getStatusByPK", BigDecimal.class)
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
    TypedQuery<String> query = em.createNamedQuery("FreeClientDoc.getDocumentNumberByPK", String.class)
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
    TypedQuery<Date> query = em.createNamedQuery("FreeClientDoc.getDocumentDateByPK", Date.class)
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
    TypedQuery<String> query = em.createNamedQuery("FreeClientDoc.getDocumentTypeStrByPK", String.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<String> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }
    return resList.get(0);
  }

  public FreeClientDoc getDocumentByID(DboDocPK dboDocPK) {
    return em.find(FreeClientDoc.class, dboDocPK);
  }
}
