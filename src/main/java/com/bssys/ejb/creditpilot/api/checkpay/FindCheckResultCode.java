package com.bssys.ejb.creditpilot.api.checkpay;

public final class FindCheckResultCode {
  public static final int FIND_CHECK_OK = 0;
  public static final int FIND_CHECK_WAIT_RESPONSE = -20215;
  public static final int FIND_CHECK_RECURRING_PAYMENT = -20135;
  public static final int FIND_CHECK_NOT_UNIQUE_PAYMENT = -20150;

  private FindCheckResultCode() {
  }
}
