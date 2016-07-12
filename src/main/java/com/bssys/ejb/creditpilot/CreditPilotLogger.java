package com.bssys.ejb.creditpilot;

import com.bssys.entity.UMTWebHistory;
import com.bssys.session.UserSessionBean;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * Created by lukas on 13.02.2015.
 */
@Stateless
@Named
@TransactionAttribute(TransactionAttributeType.REQUIRES_NEW)
public class CreditPilotLogger implements Serializable {

  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private UserSessionBean userSessionBean;

  private static final String TCN_QUERY_PARAM_NAME = "dealerTransactionId";

  public void logResponse(WebTarget target, Map<String, String> queryParams, Response response) {
    UMTWebHistory history = new UMTWebHistory();
    history.setUrl(target.getUri().toString());
    history.setStatus(response.getStatus());
    response.bufferEntity();
    history.setResponseBody(response.readEntity(byte[].class));
    history.setRequestDateTime(new Date());
    if (userSessionBean.isInited()) {
      history.setClient(userSessionBean.getAgentId());
    }
    if (queryParams.containsKey(TCN_QUERY_PARAM_NAME)) {
      history.setCheckNumber(queryParams.get(TCN_QUERY_PARAM_NAME));
    }
    em.merge(history);
  }
}
