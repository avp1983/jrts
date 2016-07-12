package com.bssys.ejb;

import com.bssys.entity.UMTWebProvCategories;
import com.bssys.entity.UMTWebProviderCategoryHelper;
import com.bssys.session.UserSessionBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Named("UMTWebProviderCategoriesFacade")
@Stateless
public class UMTWebProviderCategoriesFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private UMTSettingsConstantFacade settings;

  @PersistenceContext
  private transient EntityManager em;

  public UMTWebProvCategories getCategoryByID(int pk) {
    return em.find(UMTWebProvCategories.class, pk);
  }

  public List<UMTWebProviderCategoryHelper> getCategories(String filterStr) {
    TypedQuery<UMTWebProviderCategoryHelper> categories =
        em.createNamedQuery("UMTWebProvCategories.getCategories", UMTWebProviderCategoryHelper.class)
        .setParameter(1, settings.getStartRatingDate())
        .setParameter(2, settings.getFinishRatingDate())
        .setParameter(3, userSession.getAgentId())
        .setParameter(4, filterStr);
    return categories.getResultList();
  }

  public String convertSearchStrToFilterStr(String searchStr) {
    if ((searchStr == null) || (searchStr.trim().isEmpty())) {
      return "";
    }

    String searchUpperStr = searchStr.toUpperCase();
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < searchUpperStr.length(); i++) {
      sb.append('%');
      sb.append(searchUpperStr.charAt(i));
    }
    sb.append('%');
    return sb.toString();
  }

}
