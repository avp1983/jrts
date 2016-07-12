package com.bssys.ejb;

import com.bssys.entity.Country;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Stateless
@Named
public class CountryFacade implements Serializable {
  @PersistenceContext
  private transient EntityManager em;

  public List<Country> getListStartWith(final String str, int maxResults) {
    TypedQuery<Country> query = em.createNamedQuery("Country.codeOrNameStartWith", Country.class)
        .setParameter("query", str.toLowerCase() + "%").setMaxResults(maxResults);
    return query.getResultList();
  }

  public List<Country> getAll() {
    return em.createNamedQuery("Country.all", Country.class).getResultList();
  }

  public Country getByCode(String code) {
    if (code == null) {
      return null;
    }
    try {
      return em.createNamedQuery("Country.findByCode", Country.class).setParameter("code", code).getSingleResult();
    } catch (NoResultException exception) {
      Logger.getLogger(CountryFacade.class.getName()).log(Level.WARNING, "Введён некорректный код страны", exception);
      return null;
    }
  }
}