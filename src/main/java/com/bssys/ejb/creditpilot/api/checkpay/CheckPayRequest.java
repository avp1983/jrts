package com.bssys.ejb.creditpilot.api.checkpay;

import java.util.HashMap;
import java.util.Map;

public class CheckPayRequest {
  private int paymentType;
  private String dealerTransactionId;
  private int serviceProviderId;
  private float amount;
  private Map<String, String> additionalParams;
  private Integer stepsCount;

  public CheckPayRequest(int paymentType, String dealerTransactionId, int serviceProviderId, float amount,
                         Map<String, String> additionalParams, Integer stepsCount) {
    this.paymentType = paymentType;
    this.dealerTransactionId = dealerTransactionId;
    this.serviceProviderId = serviceProviderId;
    this.amount = amount;
    this.additionalParams = additionalParams;
    this.stepsCount = stepsCount;
  }

  public Map<String, String> toRequestParamsMap() {
    HashMap<String, String> result = new HashMap<>();
    result.put("dealerTransactionId", dealerTransactionId);
    result.put("serviceProviderId", String.valueOf(serviceProviderId));
    result.put("amount", String.valueOf(amount));
    result.put("paymentType", String.valueOf(paymentType));
    for (Map.Entry<String, String> param : additionalParams.entrySet()) {
      if (param.getKey().equals("phoneNumber")) {
        result.put(param.getKey(), param.getValue());
      } else {
        result.put("params['" + param.getKey() + "']", param.getValue());
      }
    }
    return result;
  }

  public Integer getStepsCount() {
    return stepsCount;
  }
}

