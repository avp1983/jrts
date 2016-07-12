package com.bssys.ejb.creditpilot.api.preparedoc;

import java.util.HashMap;
import java.util.Map;

public class PrepareDocRequest {
  private String dealerTransactionId;
  private int serviceProviderId;
  private float amount;
  private float amountAll;
  private Map<String, String> additionalParams;

  public PrepareDocRequest(String dealerTransactionId, int serviceProviderId, float amount, float amountAll, Map<String, String> additionalParams) {
    this.dealerTransactionId = dealerTransactionId;
    this.serviceProviderId = serviceProviderId;
    this.amount = amount;
    this.amountAll = amountAll;
    this.additionalParams = additionalParams;
  }

  public Map<String, String> toRequestParamsMap() {
    HashMap<String, String> result = new HashMap<>();
    for (Map.Entry<String, String> param : additionalParams.entrySet()) {
      if (param.getKey().equals("phoneNumber")) {
        result.put(param.getKey(), param.getValue());
      } else {
        result.put("params['" + param.getKey() + "']", param.getValue());
      }
    }
    result.put("dealerTransactionId", dealerTransactionId);
    result.put("serviceProviderId", String.valueOf(serviceProviderId));
    result.put("amount", String.valueOf(amount));
    result.put("amountAll", String.valueOf(amountAll));
    return result;
  }
}

