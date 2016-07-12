package com.bssys.ejb.creditpilot;

import com.bssys.ejb.AdminNotificationBean;
import com.bssys.ejb.creditpilot.api.ResponseResolver;
import com.bssys.ejb.creditpilot.api.types.KpDealer;
import com.bssys.entity.UMTWebHistory;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultType;
import com.bssys.rest.client.BasicAuthenticator;
import com.bssys.rts.RtsConnector;
import com.bssys.session.UserSessionBean;
import com.google.common.base.Joiner;
import org.glassfish.jersey.client.ClientProperties;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.bssys.process.step.ProcessStepResult.EMPTY_OK;
import static javax.ws.rs.core.Response.Status.OK;
import static org.joda.time.DateTimeConstants.MILLIS_PER_SECOND;

@Stateless
@Named
public class CreditPilotFacade implements Serializable {

  private static final int CREDIT_PILOT_COMMON_ERROR_WRONG_FORMAT = -55000;
  @PersistenceContext
  private transient EntityManager em;

  @Inject
  private CreditPilotSettingsFacade settingsFacade;
  @Inject
  private UserSessionBean userSessionBean;
  @Inject
  private RtsConnector rtsConnector;
  @Inject
  private AdminNotificationBean adminNotificationBean;
  @Inject
  private ResponseResolver responseResolver;
  @Inject
  private CreditPilotLogger logger;

  private static final String TCN_QUERY_PARAM_NAME = "dealerTransactionId";
  public static final String PLEASE_CONTACT_CP_ADMIN = "\nПожалуйста, свяжитесь с техничискими специалистами " +
      "КредитПилот.";

  private String getFullURL(ActionType action, Map<String, String> queryParams) {
    HashMap<String, String> convertMap = new HashMap<>();
    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
      try {
        convertMap.put(entry.getKey(),
            URLEncoder.encode(entry.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e);
      }
    }
    Joiner.MapJoiner mapJoiner = Joiner.on("&").withKeyValueSeparator("=");
    return settingsFacade.getUrl() + "?actionName=" + action.name() +
        (convertMap.isEmpty() ? "" : "&" + mapJoiner.join(convertMap));
  }

  private Response getResponseSimple(String url, Map<String, String> queryParams) throws CreditPilotCheckResponseException {
    Client client = ClientBuilder.newClient();
    try {
      client.register(new BasicAuthenticator(settingsFacade.getLogin(), settingsFacade.getPassword()));
      client.property(ClientProperties.READ_TIMEOUT, settingsFacade.getRequestTimeout() * MILLIS_PER_SECOND);

      WebTarget webTarget = client.target(url);
      int attemptNum = 0;
      Response response = null;
      do {
        attemptNum++;
        if (response != null) {
          response.close();
        }
        response = webTarget.request().get();
        logger.logResponse(webTarget, queryParams, response);
      }
      while (attemptNum < settingsFacade.getRequestAttemptCount() && isResponseStatusBad(response));
      return response;
    } finally {
      client.close();
    }
  }

  public Response getResponse(ActionType action, Map<String, String> queryParams) throws CreditPilotCheckResponseException {
    String url = getFullURL(action, queryParams);
    Response response = getResponseSimple(url, queryParams);
    ProcessStepResult checkResponseResult = checkResponseResult(response, url);

    if (checkResponseResult != EMPTY_OK) {
      response.close();
      adminNotificationBean.sendEmailToAdmin(checkResponseResult);
      throw new CreditPilotCheckResponseException(responseResolver.getResultForCommonError().getDescription());
    }
    return response;
  }

  public InputStream getResponseStream(ActionType action, Map<String, String> queryParams) throws CreditPilotCheckResponseException {
    String url = getFullURL(action, queryParams);
    Response response = getResponseSimple(url, queryParams);
    if (isResponseStatusBad(response)) {
      response.close();
      adminNotificationBean.sendEmailToAdmin(responseResolver.getResultForCommonError());
      throw new CreditPilotCheckResponseException(responseResolver.getResultForCommonError().getDescription());
    }

    InputStream stream = response.readEntity(InputStream.class);
    response.close();
    return stream;
  }

  public void checkGateEnabled() throws Exception {
    if (!settingsFacade.isGateEnabled()) {
      throw new Exception("CreditPilot was not added to active gates list");
    }
  }

  private boolean isResponseStatusBad(Response response) {
    return response.getStatus() != OK.getStatusCode();
  }

  private ProcessStepResult checkResponseResult(Response response, String requestURL) {
    if (isResponseStatusBad(response)) {
      return new ProcessStepResult(
          String.format("Не получен ответ при отправке в КредитПилот запроса %s.%s",
              requestURL, PLEASE_CONTACT_CP_ADMIN),
          ProcessStepResultType.PROCESS_RESULT_ERROR, "ПС КредитПилот не доступна"
      );
    }
    response.bufferEntity();
    KpDealer dealer = response.readEntity(KpDealer.class);
    if (dealer.getResult() != null) {
      if (Objects.equals(dealer.getResult().getResultCode().intValue(), CREDIT_PILOT_COMMON_ERROR_WRONG_FORMAT)) {
        return new ProcessStepResult(
            String.format("Получен ответ с кодом ошибки -55000 (%s) при отправке в КредитПилот запроса %s.%s",
                dealer.getResult().getResultDescription(), requestURL, PLEASE_CONTACT_CP_ADMIN),
            ProcessStepResultType.PROCESS_RESULT_ERROR, "КредитПилот вернул ответ с кодом ошибки -55000"
        );
      }
    }
    return EMPTY_OK;
  }
}
