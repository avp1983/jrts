package com.bssys.entity.PayLegal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Result")
public class UmtPayLegalCommission {
  @XmlAttribute(name = "comId")
  Integer comID;
  @XmlAttribute(name = "commission")
  Float commission;
  @XmlAttribute(name = "providerCommission")
  Float providerCommission;
  @XmlAttribute(name = "addCommission")
  Float addCommission;
  @XmlAttribute(name = "agentCommission")
  Float agentCommission;
  @XmlAttribute(name = "sysCommission")
  Float sysCommission;
  @XmlElement(name = "errMsg")
  String errMsg;

  public Float getCommission() {
    return commission;
  }

  public Float getProviderCommission() {
    return providerCommission;
  }

  public Float getAddCommission() {
    return addCommission;
  }

  public Float getAgentCommission() {
    return agentCommission;
  }

  public Float getSysCommission() {
    return sysCommission;
  }

  public String getErrMsg() {
    return errMsg;
  }

  public Integer getComID() {
    return comID;
  }
}