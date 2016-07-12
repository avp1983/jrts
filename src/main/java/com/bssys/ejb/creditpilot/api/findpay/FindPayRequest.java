package com.bssys.ejb.creditpilot.api.findpay;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

public class FindPayRequest {
  private String dealerTransactionId;

  public FindPayRequest(String dealerTransactionId) {
    this.dealerTransactionId = dealerTransactionId;
  }

  public Map<String, String> toRequestParamsMap() {
    return ImmutableMap.of("dealerTransactionId", dealerTransactionId);
  }
}

