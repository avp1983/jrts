package com.bssys.ejb.creditpilot;

import com.bssys.entity.UMTWebProvCategories;
import com.bssys.entity.UMTWebProvMapCategories;
import com.bssys.entity.UMTWebProvParamElem;
import com.bssys.entity.UMTWebProvider;
import com.bssys.entity.UMTWebProviderCountry;
import com.bssys.entity.UMTWebProviderParam;
import com.bssys.umt.UpdateException;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.io.Serializable;

/**
 * Created by lukas on 04.12.2014.
 */
@Stateless
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CreditPilotDictionaryInserterBean implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  public void saveProvider(UMTWebProvider provider) throws UpdateException {
    try {
      em.merge(provider);
      em.flush();
      // Утаптываем ногами, иначе не обновляеются, только добавляются
      for (UMTWebProviderParam param : provider.getParams()) {
        em.merge(param);
        em.flush();
        for (UMTWebProvParamElem elem : param.getItems()) {
          em.merge(elem);
          em.flush();
        }
      }
      for (UMTWebProviderCountry country : provider.getCountries()) {
        em.merge(country);
        em.flush();
      }
    }
    catch (PersistenceException e) {
      throw new UpdateException(e.getMessage());
    }
  }

  public void setProvidersPreUpdate() {
    em.createNativeQuery("update UMTWebProvider set IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProviderCountry set IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProviderParam set IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProvParamElem set IsUpdated = 0").executeUpdate();
    em.flush();
  }

  public void setProvidersAfterUpdate() {
    em.createNativeQuery("delete from UMTWebProviderCountry where IsUpdated = 0").executeUpdate();
    em.createNativeQuery("delete from UMTWebProviderParam where IsUpdated = 0").executeUpdate();
    em.createNativeQuery("delete from UMTWebProvParamElem where IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProvider set IsActive = IsUpdated").executeUpdate();
    em.flush();
  }

  public void saveCategory(UMTWebProvCategories category) throws UpdateException {
    try {
      em.merge(category);
      for (UMTWebProvMapCategories mapCategory : category.getCategoriesMaps()) {
        em.merge(mapCategory);
      }
      em.flush();
    }
    catch (PersistenceException e) {
      throw new UpdateException(e.getMessage());
    }
  }

  public void setCategoriesPreUpdate() {
    em.createNativeQuery("update UMTWebProvCategories set IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProvMapCategories set IsUpdated = 0").executeUpdate();
    em.flush();
  }

  public void setCategoriesAfterUpdate() {
    em.createNativeQuery("delete from UMTWebProvMapCategories where IsUpdated = 0").executeUpdate();
    em.createNativeQuery("update UMTWebProvCategories set IsActive = IsUpdated").executeUpdate();
    em.flush();
  }
}
