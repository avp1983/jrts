package com.bssys.entity;

import com.bssys.ejb.creditpilot.api.types.AccountType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "kp-dealer")
@XmlAccessorType(XmlAccessType.NONE)
public class UMTWebProvBalanceList {

  @XmlElement(name = "account")
  List<AccountType> accounts;

  public List<AccountType> getAccounts() {
    return accounts;
  }

  public void setAccounts(List<AccountType> accounts) {
    this.accounts = accounts;
  }
}
