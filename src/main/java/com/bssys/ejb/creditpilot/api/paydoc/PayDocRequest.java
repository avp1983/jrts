package com.bssys.ejb.creditpilot.api.paydoc;

import com.bssys.ejb.creditpilot.api.preparedoc.PrepareDocRequest;

import java.util.Map;

public class PayDocRequest extends PrepareDocRequest {
  public PayDocRequest(String dealerTransactionId, int serviceProviderId, float amount, float amountAll, Map<String, String> additionalParams) {
    super(dealerTransactionId, serviceProviderId, amount, amountAll, additionalParams);
  }
}

