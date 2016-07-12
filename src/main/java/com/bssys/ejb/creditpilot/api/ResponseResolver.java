package com.bssys.ejb.creditpilot.api;

import com.bssys.ejb.creditpilot.api.checkpay.FindCheckResult;
import com.bssys.ejb.creditpilot.api.findpay.FindPayDocState;
import com.bssys.ejb.creditpilot.api.types.KpDealer;
import com.bssys.process.step.ProcessStepResult;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.EnumMap;

import static com.bssys.ejb.creditpilot.api.checkpay.FindCheckResultCode.FIND_CHECK_NOT_UNIQUE_PAYMENT;
import static com.bssys.ejb.creditpilot.api.checkpay.FindCheckResultCode.FIND_CHECK_RECURRING_PAYMENT;
import static com.bssys.ejb.creditpilot.api.findpay.FindPayDocState.*;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_ERROR;
import static com.bssys.process.step.ProcessStepResultType.PROCESS_RESULT_OK;

@Singleton
@ApplicationScoped
@Named
public class ResponseResolver {
  private final ProcessStepResult commonError = new ProcessStepResult("Сервис временно недоступен", PROCESS_RESULT_ERROR);
  private static EnumMap<FindPayDocState, ProcessStepResult> findPayStepResult;

  @PostConstruct
  private void init() {
    initFindPayStepResult();
  }

  private void initFindPayStepResult() {
    findPayStepResult = new EnumMap<>(FindPayDocState.class);
    findPayStepResult.put(FIND_PAY_QUEUED, new ProcessStepResult("Перевод в обработке", PROCESS_RESULT_OK));
    findPayStepResult.put(FIND_PAY_INPROCESS, new ProcessStepResult("Перевод в обработке", PROCESS_RESULT_OK));
    findPayStepResult.put(FIND_PAY_OK, new ProcessStepResult("Перевод выполнен", PROCESS_RESULT_OK));
    findPayStepResult.put(FIND_PAY_ROLLBACK1, new ProcessStepResult("Перевод отменён", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_ROLLBACK2, new ProcessStepResult("Перевод отменён", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_ROLLBACK3, new ProcessStepResult("Перевод отменён", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_DILLER_NOT_ENOUGH_MONEY, new ProcessStepResult("Сервис временно недоступен", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_ROLLBACK_BY_DEMAND, new ProcessStepResult("Перевод отменён по требованию", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_ROLLBACK4, new ProcessStepResult("Перевод отменён", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_ROLLBACK5, new ProcessStepResult("Перевод отменён", PROCESS_RESULT_ERROR));
    findPayStepResult.put(FIND_PAY_UNKNOWN_BILLING_PROVIDER_ERROR, new ProcessStepResult("Перевод в обработке",
        PROCESS_RESULT_OK));
    findPayStepResult.put(FIND_PAY_SYSTEM_ERROR, new ProcessStepResult("Сервис временно недоступен", PROCESS_RESULT_ERROR));

  }

  public ProcessStepResult getResultForFindPay(FindPayDocState payResultCode) {
    return findPayStepResult.get(payResultCode);
  }

  public ProcessStepResult getResultForCommonError(){
    return commonError;
  }

  public FindCheckResult getErrorStepResultForFindCheck(KpDealer.Result findCheckResult) {
    int resultCode = findCheckResult.getResultCode().intValue();
    if (resultCode == FIND_CHECK_RECURRING_PAYMENT || resultCode == FIND_CHECK_NOT_UNIQUE_PAYMENT) {
      return new FindCheckResult(new ProcessStepResult(findCheckResult.getResultDescription(), PROCESS_RESULT_ERROR));
    } else {
      return new FindCheckResult(getResultForCommonError());
    }
  }
}
