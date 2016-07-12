package com.bssys.ejb;

import com.bssys.api.types.Document;
import com.bssys.api.types.Document.Client.SearchRequest;
import com.bssys.entity.UmtTransferClient;
import com.bssys.entity.UmtTransferClient_;
import com.google.common.base.Strings;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.metamodel.EntityType;
import java.io.Serializable;
import java.util.List;

@Stateless
@Named
public class TransferClientFacade implements Serializable {
  @PersistenceContext
  private transient EntityManager em;

  public List<UmtTransferClient> getListCardNumberStartWith(final String str, int maxResults) {
    TypedQuery<UmtTransferClient> query = em.createNamedQuery("UmtTransferClient.cardNumberStartWith", UmtTransferClient.class)
        .setParameter("query", str + "%").setMaxResults(maxResults);
    return query.getResultList();
  }

  public UmtTransferClient getById(int id) {
    return em.find(UmtTransferClient.class, id);
  }

  public UmtTransferClient getByCardNumber(String cardNumber) {
    TypedQuery<UmtTransferClient> query = em.createNamedQuery("UmtTransferClient.getByCardNumber", UmtTransferClient.class)
      .setParameter("query", cardNumber);
    return query.getSingleResult();
  }

  public List<UmtTransferClient> getByAPIRequest(SearchRequest searchRequest){
    CriteriaBuilder cb = em.getCriteriaBuilder();
    CriteriaQuery<UmtTransferClient> c = cb.createQuery(UmtTransferClient.class);
    Root<UmtTransferClient> client = c.from(UmtTransferClient.class);

    Predicate condition = getPredicate(searchRequest, cb, client);

    c.where(condition);
    TypedQuery<UmtTransferClient> q = em.createQuery(c);
    List<UmtTransferClient> result = q.getResultList();
    return result;
  }

  private Predicate getPredicate(SearchRequest searchRequest, CriteriaBuilder cb, Root<UmtTransferClient> client) {
    Predicate condition = cb.conjunction();
    if (!(Strings.isNullOrEmpty(searchRequest.getSeries()) &&
      Strings.isNullOrEmpty(searchRequest.getNumber()))){
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.licenceSeries), searchRequest.getSeries()));
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.licenceNumber), searchRequest.getNumber()));
    }

    if (!Strings.isNullOrEmpty(searchRequest.getInn())){
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.iNN), searchRequest.getInn()));
    }

    if (!Strings.isNullOrEmpty(searchRequest.getPhone())){
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.phone), searchRequest.getPhone()));
    }

    if (!Strings.isNullOrEmpty(searchRequest.getIdCard())){
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.cardNumber), searchRequest.getIdCard()));
    }

    if (searchRequest.getIdClient() != null){
      condition = cb.and(condition,cb.equal(client.get(UmtTransferClient_.id), searchRequest.getIdClient()));
    }
    return condition;
  }
}