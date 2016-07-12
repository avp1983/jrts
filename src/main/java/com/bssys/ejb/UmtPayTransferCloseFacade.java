package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.entity.UmtPayTransferClose;
import com.google.common.base.Joiner;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Named("UmtPayTransferCloseFacade")
@Stateless
public class UmtPayTransferCloseFacade implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  public String getDocumentNumberByIDR(DboDocPK dboDocPK) {
    TypedQuery<String> query = em.createNamedQuery("UmtPayTransferClose.getDocumentNumberByPK",
        String.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<String> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }

    return resList.get(0);
  }

  public String getSenderNameAndSurnameByIDR(DboDocPK dboDocPK) {
    String personName = "";
    if (dboDocPK != null) {
      UmtPayTransferClose transfer = em.find(UmtPayTransferClose.class, dboDocPK);
      if (transfer != null) {
        personName = Joiner.on(" ").skipNulls().join(transfer.getSenderLastname(), transfer.getSenderFirstname());
      }
    }
    return personName;
  }

  public String getReceiverNameAndSurnameByIDR(DboDocPK dboDocPK) {
    String personName = "";
    if (dboDocPK != null) {
      UmtPayTransferClose transfer = em.find(UmtPayTransferClose.class, dboDocPK);
      if (transfer != null) {
        personName = Joiner.on(" ").skipNulls().join(transfer.getReceiverLastname(), transfer.getReceiverFirstname());
      }
    }
    return personName;
  }

  public Integer getStatusByIDR(DboDocPK dboDocPK) {
    TypedQuery<BigDecimal> query = em.createNamedQuery("UmtPayTransferClose.getStatusByPK", BigDecimal.class)
        .setParameter(1, dboDocPK.getClient())
        .setParameter(2, dboDocPK.getDatecreate())
        .setParameter(3, dboDocPK.getTimecreate());
    List<BigDecimal> resList = query.getResultList();
    if (resList.isEmpty()) {
      return null;
    }

    return resList.get(0).intValue();
  }

  public String getAmountWithCurrency(DboDocPK dboDocPK) {
    UmtPayTransferClose transfer = em.find(UmtPayTransferClose.class, dboDocPK);
    if (transfer == null) {
      throw new RuntimeException("Не найден перевод с IDR = " + dboDocPK.toDelimString());
    }
    return transfer.getAmountWithCurrency();
  }

  public UmtPayTransferClose getDocumentByID(DboDocPK dboDocPK) {
    return em.find(UmtPayTransferClose.class, dboDocPK);
  }
}
