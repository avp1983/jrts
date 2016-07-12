package com.bssys.ejb;

import com.bssys.entity.UMTSettingsConst;
import com.bssys.entity.UMTSettingsConstPK;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;

@Stateless
@Named
public class UMTSettingsConstantFacade implements Serializable {
  private static final long serialVersionUID = 1L;

  private static final int LAST_MONTH_RATING_COUNT = 3;

  @PersistenceContext
  private transient EntityManager em;

  public UMTSettingsConst getUMTSettingsByID(UMTSettingsConstPK pk) {
    return em.find(UMTSettingsConst.class, pk);
  }

  public UMTSettingsConst getUMTSettingsByName(String settings, String constant) {
    UMTSettingsConstPK pk = new UMTSettingsConstPK(settings, constant);
    return getUMTSettingsByID(pk);
  }

  public Date getStartRatingDate() {
    Calendar cal = Calendar.getInstance();
    cal.add(Calendar.MONTH, -LAST_MONTH_RATING_COUNT);
    return cal.getTime();
  }

  public Date getFinishRatingDate() {
    return Calendar.getInstance().getTime();
  }

  public int getIntUMTSettingsByName(String settings, String constant, int defValue) {
    UMTSettingsConst settingsConst = getUMTSettingsByName(settings, constant);
    if (settingsConst == null || settingsConst.getIntValue() == null) {
      return defValue;
    } else {
      return settingsConst.getIntValue();
    }
  }

  public String getStringUMTSettingsByName(String settings, String constant, String defValue) {
    UMTSettingsConst settingsConst = getUMTSettingsByName(settings, constant);
    if (settingsConst == null || settingsConst.getStringValue() == null) {
      return defValue;
    } else {
      return settingsConst.getStringValue();
    }
  }
}
