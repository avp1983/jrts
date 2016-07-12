package com.bssys.api.chara.gate;

import com.bssys.api.types.Error;
import com.bssys.process.step.ProcessStepResult;
import com.bssys.process.step.ProcessStepResultType;

import javax.ejb.ApplicationException;
import java.math.BigInteger;
import java.util.List;

@ApplicationException(rollback = true)
public class ApiException extends Exception {

  BigInteger exceptionCode;

  public ApiException() {
    super();
  }

  public ApiException(int code, String message) {
    super(message);
    exceptionCode = BigInteger.valueOf(code);
  }

  public BigInteger getExceptionCode() {
    return exceptionCode;
  }

  public com.bssys.api.types.Error getError() {
    Error error = new Error();
    error.setCode(exceptionCode);
    error.setText(getMessage());
    return error;
  }

  public static ApiException createApiMessage(List<ProcessStepResult> results) {
    String message = null;
    for (ProcessStepResult result : results) {
      if (result.getType() != ProcessStepResultType.PROCESS_RESULT_OK) {
        if (message != null) {
          message = String.format("%s, \"%s\"", message, result.getDescription());
        } else {
          message = String.format("\"%s\"", result.getDescription());
        }
      }
    }
    return new ApiException(ApiExceptionType.API_ERR_DOC_NOT_PASSED_CONTROLS, message);
  }

  public static ApiException createApiMessage(ProcessStepResult result) {
    String message = String.format("\"%s\"", result.getDescription());
    return new ApiException(ApiExceptionType.API_ERR_DOC_NOT_PASSED_CONTROLS, message);
  }
}