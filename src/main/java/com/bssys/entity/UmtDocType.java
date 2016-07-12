package com.bssys.entity;

import java.util.HashMap;
import java.util.Map;

import static com.bssys.entity.PayTransferHelper.DOC_TYPE_PAY_LEGAL;
import static com.bssys.entity.PayTransferHelper.DOC_TYPE_PAY_TRANSFER_OPEN;

public enum UmtDocType {
  PayTransferOpen(DOC_TYPE_PAY_TRANSFER_OPEN), PayLegal(DOC_TYPE_PAY_LEGAL);

  private int id;
  UmtDocType(int id) {
    this.id = id;
  }
  public int getId() {
    return id;
  }

  private static Map<Integer, UmtDocType> idIdx = new HashMap<>();
  static{
    for(UmtDocType code: UmtDocType.values()){
      idIdx.put(code.getId(), code);
    }
  }

  public static UmtDocType byId(int id){
    UmtDocType res = idIdx.get(id);
    if (res == null){
      throw new RuntimeException("Ошибка определения типа документа");
    }
    return res;
  }

}
