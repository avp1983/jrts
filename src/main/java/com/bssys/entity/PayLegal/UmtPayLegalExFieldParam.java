package com.bssys.entity.PayLegal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Param")
public class UmtPayLegalExFieldParam {
  String key;
  String value;

  public UmtPayLegalExFieldParam(String key, String value) {
    this.key = key;
    this.value = value;
  }

  @SuppressWarnings("UnusedDeclaration")
  public UmtPayLegalExFieldParam() {
  }

  public String getKey() {
    return key;
  }

  public String getValue() {
    return value;
  }
}
