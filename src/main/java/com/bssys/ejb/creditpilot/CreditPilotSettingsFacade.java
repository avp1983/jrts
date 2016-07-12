package com.bssys.ejb.creditpilot;

import com.bssys.ejb.UMTSettingsConstantFacade;
import com.bssys.ejb.UmtAgentFacade;
import com.bssys.entity.UMTSettingsConst;
import com.bssys.entity.UMTSettingsConstPK;
import com.bssys.session.UserSessionBean;
import com.bssys.umt.ApplicationSettingsBean;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;

import static com.bssys.umt.TransferSys.TRANSFER_SYS_CREDIT_PILOT;
import static org.joda.time.DateTimeConstants.SECONDS_PER_MINUTE;

@Stateless
@Named
public class CreditPilotSettingsFacade implements Serializable {

  private static final int LAST_MSG_UPDATE_INITIAL_YEAR = 2000;
  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private UMTSettingsConstantFacade settingsConstantFacade;
  @Inject
  private UserSessionBean userSession;
  @Inject
  private UmtAgentFacade agentFacade;
  @Inject
  private ApplicationSettingsBean applicationSettingsBean;

  private String getStrSettingConstant(String constant, String defValue) {
    return
        settingsConstantFacade.getStringUMTSettingsByName(String.valueOf(TRANSFER_SYS_CREDIT_PILOT), constant, defValue);
  }

  private int getIntSettingConstant(String constant, int defValue) {
    return
        settingsConstantFacade.getIntUMTSettingsByName(String.valueOf(TRANSFER_SYS_CREDIT_PILOT), constant, defValue);
  }

  public String getUrl() {
    String workURL = getStrSettingConstant("CreditPWorkURL", "");
    if (!userSession.isInited()) {
      return workURL;
    }
    return agentFacade.isDemoAgent(userSession.getAgentId()) ? getStrSettingConstant("CreditPTestURL", "") : workURL;
  }

  public String getLogin() {
    String workLogin = getStrSettingConstant("CreditPLogin", "");
    if (!userSession.isInited()) {
      return workLogin;
    }
    return agentFacade.isDemoAgent(userSession.getAgentId()) ? getStrSettingConstant("CreditPTestLogin", "") : workLogin;
  }

  public String getPassword() {
    String workPass = getStrSettingConstant("CreditPPassword", "");
    if (!userSession.isInited()) {
      return workPass;
    }
    return agentFacade.isDemoAgent(userSession.getAgentId()) ?
        getStrSettingConstant("CreditPTestPassword", "") : workPass;
  }

  public String getApplicationId() {
    return getStrSettingConstant("CreditPAppID", "");
  }

  public int getRequestTimeout() {
    return getIntSettingConstant("CreditPTimeout", SECONDS_PER_MINUTE);
  }

  public int getRequestAttemptCount() {
    return getIntSettingConstant("CreditPRequestCount", 0) + 1;
  }

  public Boolean isGateEnabled() {
    Query query = em.createNativeQuery("select cast(count(*) as DECIMAL) from UMTTransferSys where sysid = ?1")
        .setParameter(1, TRANSFER_SYS_CREDIT_PILOT);
    return ((BigDecimal) query.getSingleResult()).intValue() == 1;
  }

  private UMTSettingsConst getLastMsgUpdateInitialValue(UMTSettingsConstPK pk) {
    UMTSettingsConst settingsConst = new UMTSettingsConst(pk);
    Calendar cal = Calendar.getInstance();
    cal.set(LAST_MSG_UPDATE_INITIAL_YEAR, Calendar.JANUARY, 1);
    settingsConst.setDateTimeValue(cal.getTime());
    return settingsConst;
  }

  public UMTSettingsConst getLastMsgUpdate() {
    UMTSettingsConstPK pk = new UMTSettingsConstPK(String.valueOf(TRANSFER_SYS_CREDIT_PILOT), "CreditPLastMsgUpdate");
    UMTSettingsConst settingsConst = settingsConstantFacade.getUMTSettingsByID(pk);
    if (settingsConst == null) {
      settingsConst = getLastMsgUpdateInitialValue(pk);
    }
    return settingsConst;
  }

}
