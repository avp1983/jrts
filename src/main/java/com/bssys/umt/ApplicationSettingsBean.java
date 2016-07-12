package com.bssys.umt;

import com.bssys.ejb.UMTSettingsConstantFacade;
import com.bssys.rts.RtsConnector;

import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;

@Singleton
@ApplicationScoped
@Named
public class ApplicationSettingsBean {
  private static final int MAX_FAVORITE_SERVICE_COUNT_DEF_VALUE = 10;
  Boolean tcnInsteadOfDn;
  Integer autoSaveSec;

  @Inject
  RtsConnector rtsConnector;
  @Inject
  UMTSettingsConstantFacade settingsConstantFacade;

  public boolean isTcnInsteadOfDn() {
    if (tcnInsteadOfDn == null) {
      tcnInsteadOfDn =
          1 == Integer.parseInt(rtsConnector.executeWithGet("RT_2UMT", "GetSettingsTCN_INSTEADOF_DN", null));
    }
    return tcnInsteadOfDn;
  }

  public int getAutoSaveSec() {
    if (autoSaveSec == null) {
      autoSaveSec = settingsConstantFacade.getIntUMTSettingsByName("Settings", "AutoSaveSec", 0);
    }
    return autoSaveSec;
  }

  public int getCreditPilotMaxFavouriteServiceCount() {
    return settingsConstantFacade.getIntUMTSettingsByName(String.valueOf(TRANSFER_SYS_CREDIT_PILOT),
        "MaxFavoriteServiceCount", MAX_FAVORITE_SERVICE_COUNT_DEF_VALUE);
  }
}
