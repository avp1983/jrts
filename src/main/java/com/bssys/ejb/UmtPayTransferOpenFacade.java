/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bssys.ejb;

import com.bssys.bls.types.DboDocPK;
import com.bssys.entity.UmtPayTransferOpen;
import com.bssys.umt.TransferDir;
import com.google.common.base.Joiner;

import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.bssys.umt.Statuses.*;

@Singleton
@Named("UmtPayTransferOpenFacade")
public class UmtPayTransferOpenFacade implements Serializable {

  @PersistenceContext
  private EntityManager em;

  @Inject
  private I18n i18n;

  // определяет направление движения перевода по универсальному шлюзу
  public int getUniversalDirectionForTransfer(final UmtPayTransferOpen transfer) {
    if (transfer.getSendertransfersys() == 0 && transfer.getReceivertransfersys() == 0) {
      return TransferDir.DIR_INTERNAL;
    }
    if (transfer.getSendertransfersys() == 0) {
      return TransferDir.DIR_OUTGOING;
    }
    if (transfer.getReceivertransfersys() == 0) {
      return TransferDir.DIR_INCOMING;
    }
    return TransferDir.DIR_TRANSITIONAL;
  }

  public String getUniversalDirectionName(final UmtPayTransferOpen transfer, Locale locale) {
    switch (getUniversalDirectionForTransfer(transfer)) {
      case TransferDir.DIR_INTERNAL:
        return i18n.getLocRes("umtpaytransfer", "TRANSFER_TYPE_INNER", locale);       //'внутренний'
      case TransferDir.DIR_INCOMING:
        return i18n.getLocRes("umtpaytransfer", "TRANSFER_TYPE_EXTERIOR", locale);    //'сторонний'
      case TransferDir.DIR_OUTGOING:
        return i18n.getLocRes("umtpaytransfer", "TRANSFER_TYPE_OUTER", locale);       //'внешний'
      case TransferDir.DIR_TRANSITIONAL:
        return i18n.getLocRes("umtpaytransfer", "TRANSFER_TYPE_THROUGH", locale); //'транзитный'
      default:
        throw new AssertionError("Неизвестное направление перевода " + getUniversalDirectionForTransfer(transfer));
    }
  }

  public String getDocumentNumberByIDRForUser(DboDocPK dboDocPK) {
    TypedQuery<String> query = em.createNamedQuery("Umtpaytransferopen.getDocumentNumberByPK",
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

  public UmtPayTransferOpen getDocumentByID(DboDocPK pk) {
    return em.find(UmtPayTransferOpen.class, pk);
  }

  public List<Object[]> getLastSendTransfers(int lastCount, int userKey) {
    //noinspection unchecked
    return em.createNamedQuery("Umtpaytransferopen.getLastSendTransfers")
        .setParameter(1, userKey)
        .setMaxResults(lastCount)
        .getResultList();
  }

  public String getSenderNameAndSurnameByIDR(DboDocPK dboDocPK) {
    String personName = "";
    if (dboDocPK != null) {
      UmtPayTransferOpen transfer = em.find(UmtPayTransferOpen.class, dboDocPK);
      if (transfer != null) {
        personName = Joiner.on(" ").skipNulls().join(transfer.getSenderlastname(), transfer.getSenderfirstname());
      }
    }
    return personName;
  }

  public String getReceiverNameAndSurnameByIDR(DboDocPK dboDocPK) {
    String personName = "";
    if (dboDocPK != null) {
      UmtPayTransferOpen transfer = em.find(UmtPayTransferOpen.class, dboDocPK);
      if (transfer != null) {
        personName = Joiner.on(" ").skipNulls().join(transfer.getReceiverlastname(), transfer.getReceiverfirstname());
      }
    }
    return personName;
  }

  public String getICStatusDescriptionByStatus(int status, Locale locale) {
    switch (status) {
      case STS_BSI_FOO:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_DRAFT", locale);
      case STS_UMT_TEMPLATE:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_UMT_TEMPLATE", locale);
      case STS_BSI_NEW:
      case STS_NEW:
      case STS_PKO_REQUEST:
      case STS_READY_TOSEND:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_NEW", locale);
      case STS_READY_TOPAY:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_READY_TO_PAY", locale);
      case STS_BSI_SIGNED_1:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SIGNED1", locale);
      case STS_BSI_SIGNED_2:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SIGNED2", locale);
      case STS_BSI_SIGNED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SIGNED", locale);
      case STS_BSI_TEMPLATE:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_TEMPLATE", locale);
      case STS_PAID:
      case STS_PAIDOUT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PAID", locale);
      case STS_MTUNLOADING:
      case STS_MTUNLOADED:
      case STS_EXPORTEDPLUS:
      case STS_MTCONFIRMED:
      case STS_TOPAY:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_TO_PAY", locale);
      case STS_NOPAID:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_NOT_PAYED", locale);
      case STS_ACCESSORIES_ERROR:
      case STS_ACCESSORIES_ERROR_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_ACCESSORIES_ERROR", locale);
      case STS_CLIENT_SIGN_ILLEGAL:
      case STS_CLIENT_SIGN_ILLEGAL_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SIGN_ILLEGAL", locale);
      case STS_CLIENT_SIGN_INCORRECT:
      case STS_CLIENT_SIGN_INCORRECT_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SIGN_INCORRECT", locale);
      case STS_CLIENT_NOT_ACCEPTED:
      case STS_CLIENT_NOT_ACCEPTED_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_NOT_ACCEPTED", locale);
      case STS_CLIENT_NOT_ACCEPTED_ABS:
      case STS_CLIENT_NOT_ACCEPTED_ABS_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_NOT_ACCEPTED_ABS", locale);
      case STS_CLIENT_REFUSED_ABS:
      case STS_CLIENT_REFUSED_ABS_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_REFUSED_ABS", locale);
      case STS_CLIENT_ACCEPTED:
      case STS_CLIENT_ACCEPTED_KVIT:
      case STS_CLIENT_ACCEPTED_PRINT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_ACCEPTED", locale);
      case STS_CLIENT_DEFERED:
      case STS_CLIENT_DEFERED_KVIT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_DEFERRED", locale);
      case STS_BLOCKEDPAID:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_BLOCKED_PAID", locale);
      case STS_BSI_DELETED:
      case STS_DELETED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_BSI_DELETED", locale);
      case STS_SENT:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_SENT", locale);
      case STS_BLOCKEDCHANGE:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_BLOCKED_CHANGE", locale);
      case STS_CHANGEREQUEST_SENT:
      case STS_CHANGEREQUEST_ACCEPTED:
      case STS_CHANGE_RECEIVE:
      case STS_CHANGE_ROLLBACK:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_CHANGING", locale);
      case STS_BLOCKED_RETURN:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_BLOCKED_RETURN", locale);
      case STS_CANCEL_REQUEST_SENT:
      case STS_CANCEL_REQUEST_ACCEPTED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_CANCELING", locale);
      case STS_CANCELLED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_CANCELLED", locale);
      case STS_RETURNING:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_RETURNING", locale);
      case STS_RETURNED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_RETURNED", locale);
      case STS_UNDOINGREQUEST_SENT:
      case STS_UNDOINGREQUEST_ACCEPTED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_UNDOING", locale);
      case STS_UMT_CANCELED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_UMT_CANCELED", locale);
      case STS_CACCEPTED_TO_PROCESS:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PROCESS", locale);
      case STS_C_PROCESSED:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PROCESSED", locale);
      case STS_C_PROCESSED_PLUS:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PROCESSED_PLUS", locale);
      case STS_PAYLEGAL_READY_TOPAY:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PAYLEGAL_READY_TOPAY", locale);
      case STS_PAYLEGAL_PAID:
        return i18n.getLocRes("docstatuses", "DOC_STATUS_IC_PAYLEGAL_PAID", locale);
      default:
        throw new AssertionError("Неизвестный статус перевода: " + status);
    }
  }

  public String getAmountWithCurrency(DboDocPK pk) {
    UmtPayTransferOpen transfer = em.find(UmtPayTransferOpen.class, pk);
    if (transfer == null) {
      throw new RuntimeException("Не найден перевод с IDR = " + pk.toDelimString());
    }
    return transfer.getAmountWithCurrency();
  }

  public Date getDocLastModificationDateByIDR(DboDocPK pk, int userKey) {
    UmtPayTransferOpen transfer = em.find(UmtPayTransferOpen.class, pk);
    if (transfer == null) {
      throw new RuntimeException("Не найден перевод с IDR = " + pk.toDelimString());
    }
    return (transfer.getUserpayer() == userKey) ? transfer.getStatuslastchangetime() : transfer.getValuedatetime();
  }
}
