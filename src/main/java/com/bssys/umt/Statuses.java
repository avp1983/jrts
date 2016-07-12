package com.bssys.umt;

public final class Statuses {
  public static final int STS_NEW = 1001;
  public static final int STS_TEMPLATE = 1000;
  public static final int STS_UMTEFFECTIVE = 2001;
  public static final int STS_FORSEND = 7001;
  public static final int STS_PAYLEGAL_READY_TOPAY = 7301;
  public static final int STS_SENDING = 9001;
  public static final int STS_SENDED = 9011;
  public static final int STS_DELIVERED = 9021;
  public static final int STS_UMT_TEMPLATE = 11011;
  public static final int STS_SENT = 11031;
  public static final int STS_CLIENT_SIGN_ILLEGAL = 13011;
  public static final int STS_CLIENT_SIGN_ILLEGAL_KVIT = 13013;
  public static final int STS_CLIENT_SIGN_INCORRECT = 13021;
  public static final int STS_CLIENT_SIGN_INCORRECT_KVIT = 13023;
  public static final int STS_CLIENT_ACCEPTED = 15001;
  public static final int STS_CLIENT_ACCEPTED_PRINT = 15002;
  public static final int STS_CLIENT_ACCEPTED_KVIT = 15003;
  public static final int STS_CLIENT_NOT_ACCEPTED = 15011;
  public static final int STS_CLIENT_NOT_ACCEPTED_KVIT = 15013;
  public static final int STS_CACCEPTED_TO_PROCESS = 15021;
  public static final int STS_ACCESSORIES_ERROR = 15031;
  public static final int STS_ACCESSORIES_ERROR_KVIT = 15033;
  public static final int STS_MTUNLOADING = 15071;
  public static final int STS_MTUNLOADED = 15081;
  public static final int STS_MTCONFIRMED = 15091;
  public static final int STS_EXPORTEDPLUS = 17003;
  public static final int STS_PKO_REQUEST = 17005;
  public static final int STS_READY_TOSEND = 17007;
  public static final int STS_READY_TOPAY = 17008;
  public static final int STS_CLIENT_NOT_ACCEPTED_ABS = 17021;
  public static final int STS_CLIENT_NOT_ACCEPTED_ABS_KVIT = 17023;
  public static final int STS_CLIENT_DEFERED = 17031;
  public static final int STS_CLIENT_DEFERED_KVIT = 17033;
  public static final int STS_CLIENT_REFUSED_ABS = 17061;
  public static final int STS_CLIENT_REFUSED_ABS_KVIT = 17063;
  public static final int STS_PAID = 17083;
  public static final int STS_NOPAID = 17093;
  public static final int STS_BLOCKEDCHANGE = 17111;
  public static final int STS_CHANGEREQUEST_SENT = 17121;
  public static final int STS_CHANGEREQUEST_ACCEPTED = 17131;
  public static final int STS_CHANGE_RECEIVE = 17132;
  public static final int STS_CHANGE_ROLLBACK = 17141;
  public static final int STS_CANCEL_REQUEST_SENT = 17151;
  public static final int STS_CANCEL_REQUEST_ACCEPTED = 17161;
  public static final int STS_CANCELLED = 17171;
  public static final int STS_RETURNING = 17191;
  public static final int STS_RETURNED = 17201;
  public static final int STS_PAIDOUT = 17211;
  public static final int STS_BLOCKED_RETURN = 17221;
  public static final int STS_UNDOINGREQUEST_SENT = 17231;
  public static final int STS_UNDOINGREQUEST_ACCEPTED = 17241;
  public static final int STS_PAYLEGAL_PAID = 17301;
  public static final int STS_C_PROCESSED = 18011; // {Обработан}                  {Документ обработан}
  public static final int STS_C_PROCESSED_PLUS = 18013; // {Обработан+}                 {Документ обработан}
  public static final int STS_C_IN_QUEUE = 18041; // {В очереди}
  public static final int STS_C_EXECUTING = 18051; // {Выполняется}
  public static final int STS_C_ERROR = 18071; // {Ошибка}
  public static final int STS_C_NODATA = 18081; // {Нет данных}
  public static final int STS_UMT_CANCELED = 25026;
  public static final int STS_KEYPROCESSED = 27071;
  public static final int STS_BLOCKEDPAID = 27091;
  public static final int STS_TOPAY = 27101;
  public static final int STS_DELETED = 30001;
  public static final int STS_BSI_FOO = 32000;
  public static final int STS_BSI_TEMPLATE = 32001;
  public static final int STS_BSI_NEW = 32101;
  public static final int STS_BSI_SIGNED_1 = 32201;
  public static final int STS_BSI_SIGNED_2 = 32202;
  public static final int STS_BSI_SIGNED = 32210;
  public static final int STS_BSI_DELETED = 32501;

  private Statuses() {
  }
}
