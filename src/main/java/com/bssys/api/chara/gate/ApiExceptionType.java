package com.bssys.api.chara.gate;

public class ApiExceptionType {
/* см таблицу UMTAPIERRORCODES*/

  public static final int API_ERR_FAULT = 1;
  public static final int API_ERR_ACCESS_DENIED = 5;
  public static final int API_ERR_DOC_NOT_PASSED_CONTROLS = 9;
  public static final int API_ERR_TCN_NOT_FOUND = 18;
  public static final int API_ERR_POINT_COUNTRY_CODE_NOT_FOUND = 89;
  public static final int API_ERR_TRANSFER_CREATED_BY_OTHER_CLIENT = 108;
  public static final int API_ERR_PROVIDER_NOT_FOUND = 115;
  public static final int API_ERR_PROVIDER_NOT_ACTIVE = 116;
  public static final int API_ERR_PAY_LEGAL_CANNOT_CHANGE_PARAMS = 117;
  public static final int API_ERR_PAY_LEGAL_UNNECESSARY_PARAMS = 118;
  public static final int API_ERR_PAY_LEGAL_INSUFFICIENT_PARAMS = 119;
  public static final int API_ERR_PAY_LEGAL_CHANGED_PROCESSED_PARAMS = 120;
  public static final int API_ERR_PAY_LEGAL_CHANGED_READ_ONLY_PARAMS = 121;
  public static final int API_ERR_PAY_LEGAL_BAD_CURR_CODE = 122;
  public static final int API_ERR_CLIENT_SEARCH_REQUEST_EMPTY = 201;
  public static final int API_ERR_CLIENT_TRANSFER_REQUEST_EMPTY = 202;
  public static final int API_ERR_CLIENT_NOT_FOUND = 45;
  public static final int API_ERR_TRANSFER_NOT_FOUND = 75;
  public static final int API_ERR_TRANSFER_REQUEST_NOT_FOUND = 81;
  public static final int API_ERR_TRANSFER_REQUEST_IN_QUEUE = 82;
  public static final int API_ERR_TRANSFER_REQUEST_IN_PROCESS = 83;
  public static final int API_ERR_TRANSFER_REQUEST_ERROR = 84;
  public static final int API_ERR_TRANSFER_REQUEST_NODATA = 85;
}
