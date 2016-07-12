package com.bssys.entity.PayLegal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static com.bssys.ejb.creditpilot.api.types.CheckPayDataType.CheckPayTable.CheckPayTableRow.CheckPayParam;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Params")
public class UmtPayLegalExFieldParams {
  List<UmtPayLegalExFieldParam> payParams;

  public UmtPayLegalExFieldParams(List<CheckPayParam> checkPayParams) {
    this();
    for (CheckPayParam payParam : checkPayParams) {
      payParams.add(new UmtPayLegalExFieldParam(payParam.getKey(), payParam.getValue()));
    }
  }

  public List<UmtPayLegalExFieldParam> getPayParams() {
    return payParams;
  }

  @SuppressWarnings("UnusedDeclaration")
  public UmtPayLegalExFieldParams() {
    payParams = new ArrayList<>();
  }
}


