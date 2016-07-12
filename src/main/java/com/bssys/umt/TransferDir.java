package com.bssys.umt;

public final class TransferDir {
  // направления движения переводов в универсальном шлюзе
  public static final int DIR_INTERNAL = 0;      // внутренний перевод
  public static final int DIR_OUTGOING = 1;      // внешний (исходящий) перевод
  public static final int DIR_INCOMING = 2;      // сторонний (входящий) перевод
  public static final int DIR_TRANSITIONAL = 3;  // транзитный перевод

  private TransferDir() {
  }
}
