package com.bssys.umt;

public enum UserRightEnum {
  USER_RIGHT_TRANSFER_VIEW(1),
  USER_RIGHT_TRANSFER_ACTION(2),
  USER_RIGHT_MESSAGE_ADMIN(3),
  USER_RIGHT_REPORT(4),
  USER_RIGHT_TRANSFER_VIEW_TOPAY(7),
  USER_RIGHT_MESSAGE_AGENT(8),
  USER_RIGHT_REPORT_MUTUAL(9),
  USER_RIGHT_SIGN_USER_REQUEST(11),
  USER_RIGHT_ADDITIONAL_COMMISSION(16),
  USER_RIGHT_REPORT_MUTUAL_SETTLEMENTS(17);

  private final int value;

  private UserRightEnum(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

}
