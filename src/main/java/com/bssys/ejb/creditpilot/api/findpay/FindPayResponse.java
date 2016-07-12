package com.bssys.ejb.creditpilot.api.findpay;

import com.bssys.ejb.creditpilot.api.types.PaymentType;
import com.bssys.process.step.ProcessStepResult;

public class FindPayResponse {
  private ProcessStepResult result;
  private PaymentType foundPayment;
  private boolean isRemoteOperationComplete;

  public FindPayResponse(ProcessStepResult result, PaymentType foundPayment, boolean isRemoteOperationComplete) {
    this.result = result;
    this.foundPayment = foundPayment;
    this.isRemoteOperationComplete = isRemoteOperationComplete;
  }

  public FindPayResponse(ProcessStepResult result) {
    this.result = result;
  }

  public ProcessStepResult getResult() {
    return result;
  }

  public PaymentType getFoundPayment() {
    return foundPayment;
  }

  public boolean isRemoteOperationComplete() {
    return isRemoteOperationComplete;
  }
}
