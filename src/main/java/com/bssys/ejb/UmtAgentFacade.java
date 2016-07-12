package com.bssys.ejb;

import com.google.common.base.Strings;
import org.apache.commons.lang3.time.DateUtils;

import javax.ejb.Stateless;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

import static com.bssys.ejb.AgentAllowActions.CAN_PAY_TRANSFERS;
import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS;
import static com.bssys.ejb.AgentAllowActions.CAN_SEND_TRANSFERS_LEGAL_ENTITIES;
import static com.bssys.umt.Statuses.*;
import static javax.persistence.ParameterMode.IN;
import static javax.persistence.ParameterMode.OUT;

/**
 * @author alexeymt
 */
@Named("UmtAgentFacade")
@Stateless
public class UmtAgentFacade implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  @SuppressWarnings("unchecked")
  public BigDecimal getAgentLoyalityPointsTotal(final int agentId) {
    return (BigDecimal) em.createNativeQuery("select cast(SUM(Points) as decimal) " +
        "from UMTLoyaltyTransfer where Client = ?")
        .setParameter(1, agentId).getSingleResult();
  }

  @SuppressWarnings("unchecked")
  public Boolean hasAgentLoyalityActions(final int agentId) {
    BigDecimal res = (BigDecimal) em.createNativeQuery("select cast (count(*) as decimal) " +
        "from umtloyalty l " +
        "join viewexpragentlinksra ra on ra.childlink = ? " +
        "join umtloyaltyclients c on c.umtloyaltycode = l.autokey and c.client = ra.parentlink " +
        "where l.datebegin <= ? and l.dateend + 1 > ? " +
        "   and ra.childlink = ? " +
        "   and c.client = ra.parentlink ")
        .setParameter(1, agentId)
        .setParameter(2, new Date(new java.util.Date().getTime()))
        .setParameter(3, new Date(new java.util.Date().getTime()))
        .setParameter(4, agentId)
        .getSingleResult();
    return res.intValue() > 0;
  }

  @SuppressWarnings("unchecked")
  public BigDecimal getAgentCurrentLimit(final int agentId) {
    List<BigDecimal> resList = em.createNativeQuery("select cast(UMTLimit.LimitRestAmount as decimal) " +
        "from UMTLimit " +
        "left outer join UMTAgentLinks on (UMTAgentLinks.ParentLink=UMTLimit.Client) " +
        "where COALESCE(UMTAgentLinks.ChildLink, UMTLimit.Client) = ? and UMTLimit.Actual=1 " +
        "order by LimitRestAmount")
        .setParameter(1, agentId).getResultList();
    if (resList.isEmpty()) {
      return BigDecimal.valueOf(0);
    }
    return resList.get(0);
  }

  private static final String AGENT_FINISHED_REPORT_COUNT_QUERY = String.format("select cast(COUNT(*) as decimal) " +
          "from ExprRepRequest where Client = ? and Status not in (%s, %s) and ReportId not in (3, 40, 120)",
      STS_C_IN_QUEUE, STS_C_EXECUTING
  );

  @SuppressWarnings("unchecked")
  public int getAgentFinishedReportCount(final int agentId) {
    // количество отчётов в скроллере «Готовые отчёты» в статусах, отличных от «в очереди», «выполняется»"
    BigDecimal res = (BigDecimal) em.createNativeQuery(AGENT_FINISHED_REPORT_COUNT_QUERY)
        .setParameter(1, agentId).getSingleResult();
    return res.intValue();
  }

  private static final String AGENT_NEW_CERT_REQUEST_COUNT_QUERY = String.format("select cast(COUNT(*) as decimal) " +
          "from ClientKeyExchange where Status = %s and Client IN (select ChildLink " +
          "from UMTAgentLinks where ParentLink = ?)",
      STS_BSI_NEW
  );

  @SuppressWarnings("unchecked")
  public int getAgentNewCertRequestCount(final int agentId) {
    BigDecimal res = (BigDecimal) em.createNativeQuery(AGENT_NEW_CERT_REQUEST_COUNT_QUERY)
        .setParameter(1, agentId).getSingleResult();
    return res.intValue();
  }

  private static final String MSG_FROM_ADMIN_QUERY = String.format("SELECT cast(COUNT(*) as decimal) FROM FREEBANKDOC " +
          "WHERE STATUS IN (%s, %s, %s, %s, %s) AND coalesce(VIEWFLAGS, 0) <> 1 " +
          "and DestCustID = ?", STS_FORSEND, STS_SENDING, STS_SENDED,
      STS_DELIVERED, STS_KEYPROCESSED
  );

  @SuppressWarnings("unchecked")
  public int getAgentNewMsgFromAdminCount(final int agentId) {
    BigDecimal res = (BigDecimal) em.createNativeQuery(MSG_FROM_ADMIN_QUERY)
        .setParameter(1, agentId).getSingleResult();
    return res.intValue();
  }

  @SuppressWarnings("unchecked")
  public int getAgentNewMsgFromAgentsCount(final int agentId, final int userKey) {
    BigDecimal res = (BigDecimal) em.createNativeQuery("select cast(count(*) as decimal) from FreeAgExDoc " +
        "where" +
        "  (" +
        "    ( ID in (select MessageID from UMTMessageReceivers where" +
        "          ReceiverID = ? and ReceiverType = 1 and MessageType = 1 )" +
        "    ) " +
        "    or ( ID in ( select MessageID from UMTMessageReceivers where" +
        "          ReceiverID = ? and ReceiverType = 2 and MessageType = 1 )" +
        "    ) " +
        "  ) " +
        "  and Status = 32101 and coalesce(ViewFlags, 0) = 0")
        .setParameter(1, agentId)
        .setParameter(2, userKey).getSingleResult();
    return res.intValue();
  }

  @SuppressWarnings("unchecked")
  public int getNewsAddedTodayCount() {
    java.util.Date today = DateUtils.truncate(new java.util.Date(), Calendar.DATE);
    BigDecimal res = (BigDecimal) em.createNativeQuery("select cast(count(*) as decimal) from news " +
        "where NewsDate >= ?")
        .setParameter(1, new java.sql.Date(today.getTime())).getSingleResult();
    return res.intValue();
  }


  public boolean isRootAgent(int agentId) {
    BigDecimal res = (BigDecimal) em.createNativeQuery("select cast(LinkClient as decimal) from POSTCLNT where client = ?")
        .setParameter(1, agentId).getSingleResult();
    return (null == res);
  }

  public boolean isDemoAgent(int agentId) {
    BigDecimal res = (BigDecimal) em.createNativeQuery("select cast(AgentDemoEnabled as decimal) from CUSTOMER where client = ?")
        .setParameter(1, agentId).getSingleResult();
    return ((res != null) && (res.intValue() == 1));
  }

  public int getRejectedAdditionalCommissions(int agentId) {
    return ((BigDecimal) em.createNativeQuery("select cast(count(*) as decimal) from UMTCOMMEXT where client = ? " +
        "and Status in (?, ?)")
        .setParameter(1, agentId)
        .setParameter(2, STS_CLIENT_NOT_ACCEPTED)
        .setParameter(3, STS_CLIENT_NOT_ACCEPTED_KVIT).getSingleResult())
        .intValue();
  }

  public int getActiveAdditionalCommissions(int agentId) {
    return ((BigDecimal) em.createNativeQuery("select cast(count(*) as decimal) from UMTCOMMEXT where client = ? " +
        "and Status = ?")
        .setParameter(1, agentId)
        .setParameter(2, STS_UMTEFFECTIVE)
        .getSingleResult())
        .intValue();
  }

  public boolean getCanWriteOffAccount(int agentId) {
    BigDecimal fieldValue = (BigDecimal) em.createNativeQuery("select cast(CanWriteOffAccount as decimal) from CUSTOMER where client = ?")
        .setParameter(1, agentId)
        .getSingleResult();
    return Objects.equals(BigDecimal.valueOf(1), fieldValue);
  }

  private int getInheritAllowedActions(int agentId) {
    Query query = em.createNativeQuery(
        "select cast(InheritAllowedActions as decimal) from Customer where CustId = ?")
        .setParameter(1, agentId);
    if (query.getSingleResult() == null)
      return 0;
    else
      return ((BigDecimal) query.getSingleResult()).intValue();
  }

  public EnumSet<AgentAllowActions> getAgentAllowActions(int agentID) {
    StoredProcedureQuery procedureQuery = em.createStoredProcedureQuery("GetAgentAllowedActions");
    procedureQuery.registerStoredProcedureParameter("pCustId", Integer.class, IN);
    procedureQuery.registerStoredProcedureParameter("pClient", Integer.class, IN);
    procedureQuery.registerStoredProcedureParameter("GetUpperAgentSettings", Integer.class, IN);
    procedureQuery.registerStoredProcedureParameter("CanSendTransfers", Integer.class, OUT);
    procedureQuery.registerStoredProcedureParameter("CanPayTransfers", Integer.class, OUT);
    procedureQuery.registerStoredProcedureParameter("CanSendTransfersLegalEntities", Integer.class, OUT);
    procedureQuery.registerStoredProcedureParameter("Errors", String.class, OUT);
    procedureQuery.setParameter("pCustId", agentID);
    procedureQuery.setParameter("pClient", agentID);
    procedureQuery.setParameter("GetUpperAgentSettings", getInheritAllowedActions(agentID));

    procedureQuery.execute();
    if (!Strings.isNullOrEmpty((String) procedureQuery.getOutputParameterValue("Errors"))) {
      throw new RuntimeException((String) procedureQuery.getOutputParameterValue("Errors"));
    }

    EnumSet<AgentAllowActions> res = EnumSet.noneOf(AgentAllowActions.class);

    if (Objects.equals(procedureQuery.getOutputParameterValue("CanSendTransfers"), 1)) {
      res.add(CAN_SEND_TRANSFERS);
    }
    if (Objects.equals(procedureQuery.getOutputParameterValue("CanPayTransfers"), 1)) {
      res.add(CAN_PAY_TRANSFERS);
    }
    if (Objects.equals(procedureQuery.getOutputParameterValue("CanSendTransfersLegalEntities"), 1)) {
      res.add(CAN_SEND_TRANSFERS_LEGAL_ENTITIES);
    }
    return res;
  }
}