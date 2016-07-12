package com.bssys.ejb.creditpilot.api.checkpay;

import com.bssys.entity.PayLegal.UmtPayLegalExField;
import com.bssys.process.step.ProcessStepResult;

import java.util.List;
import java.util.Map;

public class FindCheckResult {
  private ProcessStepResult result;
  private int stepsLeft;
  private Map<String, String> servicePayParams;
  private List<UmtPayLegalExField> additionalPayRequisites;

  public ProcessStepResult getResult() {
    return result;
  }

  public Map<String, String> getServicePayParams() {
    return servicePayParams;
  }

  public List<UmtPayLegalExField> getAdditionalPayRequisites() {
    return additionalPayRequisites;
  }

  public int getStepsLeft() {
    return stepsLeft;
  }

  public FindCheckResult(ProcessStepResult result, int stepsLeft, Map<String, String> servicePayParams,
                         List<UmtPayLegalExField> additionalPayParams) {
    this.result = result;
    this.stepsLeft = stepsLeft;
    this.servicePayParams = servicePayParams;
    this.additionalPayRequisites = additionalPayParams;
  }

  public FindCheckResult(ProcessStepResult result) {
    this.result = result;
  }
}

