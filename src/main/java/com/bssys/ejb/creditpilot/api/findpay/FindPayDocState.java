package com.bssys.ejb.creditpilot.api.findpay;

import java.util.HashMap;
import java.util.Map;

public enum FindPayDocState {
  FIND_PAY_QUEUED(20000),
  FIND_PAY_INPROCESS(20002),
  FIND_PAY_OK(1),
  FIND_PAY_ROLLBACK1(0),
  FIND_PAY_ROLLBACK2(-100),
  FIND_PAY_ROLLBACK3(-200),
  FIND_PAY_DILLER_NOT_ENOUGH_MONEY(-300),
  FIND_PAY_ROLLBACK_BY_DEMAND(-400),
  FIND_PAY_ROLLBACK4(-500),
  FIND_PAY_ROLLBACK5(-600),
  FIND_PAY_UNKNOWN_BILLING_PROVIDER_ERROR(2),
  FIND_PAY_SYSTEM_ERROR(-20300);

  private int code;

  private FindPayDocState(int code) {
    this.code = code;
  }

  private int getCode() {
    return code;
  }

  private static Map<Integer, FindPayDocState> codeIdx = new HashMap<>();
  static{
    for(FindPayDocState code: FindPayDocState.values()){
      codeIdx.put(code.getCode(), code);
    }
  }

  public static FindPayDocState byCode(int code) {
    return codeIdx.get(code);
  }

}
