package com.bssys.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Table(name = "UMTWebHistory")
@Entity
public class UMTWebHistory {

  public UMTWebHistory() {
  }

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  private Integer client;
  @Temporal(TemporalType.TIMESTAMP)
  private Date requestDateTime;
  private String url;
  private Integer status;
  private String checkNumber;
  @Lob
  @Column(name = "ResponseBody")
  private byte[] responseBody;

  public Integer getId() {
    return id;
  }

  public Integer getClient() {
    return client;
  }

  public void setClient(Integer client) {
    this.client = client;
  }

  public Date getRequestDateTime() {
    return requestDateTime;
  }

  public void setRequestDateTime(Date requestDateTime) {
    this.requestDateTime = requestDateTime;
  }

  public String getUrl() {
    return url;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public Integer getStatus() {
    return status;
  }

  public void setStatus(Integer status) {
    this.status = status;
  }

  public byte[] getResponseBody() {
    return responseBody;
  }

  public void setResponseBody(byte[] responceBody) {
    this.responseBody = responceBody;
  }

  public String getCheckNumber() {
    return checkNumber;
  }

  public void setCheckNumber(String checkNumber) {
    this.checkNumber = checkNumber;
  }
}
