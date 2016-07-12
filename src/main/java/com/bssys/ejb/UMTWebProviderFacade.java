package com.bssys.ejb;

import com.bssys.entity.UMTWebProvCategories;
import com.bssys.entity.UMTWebProvider;
import com.bssys.entity.UMTWebProviderHelper;
import com.bssys.entity.UMTWebProviderParam;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.ApplicationSettingsBean;
import org.apache.commons.lang3.StringUtils;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.io.Serializable;
import java.util.List;

@Named("UMTWebProviderFacadeFacade")
@Stateless
public class UMTWebProviderFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private UserSessionBean userSession;

  @Inject
  private UMTSettingsConstantFacade settings;

  @Inject
  private ApplicationSettingsBean applicationSettingsBean;

  public List<UMTWebProviderHelper> getProvidersByCategory(int category, String filterStr) {
    TypedQuery<UMTWebProviderHelper> providers =
        em.createNamedQuery("UMTWebProvider.getProvidersByCategory", UMTWebProviderHelper.class)
            .setParameter(1, settings.getStartRatingDate())
            .setParameter(2, settings.getFinishRatingDate())
            .setParameter(3, userSession.getAgentId())
            .setParameter(4, category)
            .setParameter(5, filterStr);
    return providers.getResultList();
  }

  public List<UMTWebProviderHelper> getProviders(String filterStr) {
    TypedQuery<UMTWebProviderHelper> providers =
        em.createNamedQuery("UMTWebProvider.getProviders", UMTWebProviderHelper.class)
        .setParameter(1, settings.getStartRatingDate())
        .setParameter(2, settings.getFinishRatingDate())
        .setParameter(3, userSession.getAgentId())
        .setParameter(4, filterStr);
    return  providers.getResultList();
  }

  public List<UMTWebProvCategories> getProviderCategories(UMTWebProvider provider) {
    TypedQuery<UMTWebProvCategories> providers =
        em.createNamedQuery("UMTWebProvider.getProviderCategories", UMTWebProvCategories.class)
            .setParameter("providerID", provider.getID());
    return providers.getResultList();
  }

  public UMTWebProviderParam getParamByName(UMTWebProvider provider, String name) {
    for (UMTWebProviderParam param : provider.getParams()) {
      if (StringUtils.equals(param.getName(), name)) {
        return param;
      }
    }
    throw new AssertionError("У провайдера " + provider + "не найден параметр " + name);
  }

  public List<UMTWebProviderHelper> getFavourites() {
    TypedQuery<UMTWebProviderHelper> providers =
        em.createNamedQuery("UMTWebProvider.getFavourites", UMTWebProviderHelper.class)
        .setParameter(1, settings.getStartRatingDate())
        .setParameter(2, settings.getFinishRatingDate())
        .setParameter(3, userSession.getAgentId())
        .setMaxResults(applicationSettingsBean.getCreditPilotMaxFavouriteServiceCount());
    return providers.getResultList();
  }

  public UMTWebProviderHelper getProviderById(Integer id) {
    UMTWebProvider prov = em.find(UMTWebProvider.class, id);
    return new UMTWebProviderHelper(id, prov.getName(), 0);

  }
}