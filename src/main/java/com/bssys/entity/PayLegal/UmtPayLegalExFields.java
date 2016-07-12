package com.bssys.entity.PayLegal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "ROOT")
public class UmtPayLegalExFields {
  @XmlElement(name = "REC")
  private List<UmtPayLegalExField> recList;

  public List<UmtPayLegalExField> getRecList() {
    return recList;
  }

  public void setRecList(List<UmtPayLegalExField> recList) {
    this.recList = recList;
  }

  public UmtPayLegalExFields() {
    recList = new ArrayList<>();
  }
}


