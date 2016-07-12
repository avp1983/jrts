package com.bssys.entity;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.XmlAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.text.SimpleDateFormat;
import java.util.Date;

@XmlAccessorType(XmlAccessType.PROPERTY)
public class UMTWebProvMessage implements Comparable<UMTWebProvMessage> {

  Date date;
  Integer providerID;
  String type;
  String text;

  @XmlElement(name = "date")
  @XmlJavaTypeAdapter(UMTWebProvMessageDateAdapter.class)
  public Date getDate() {
    return date;
  }

  public void setDate(Date date) {
    this.date = date;
  }

  @XmlElement(name = "provider")
  public Integer getProviderID() {
    return providerID;
  }

  public void setProviderID(Integer providerID) {
    this.providerID = providerID;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Override
  public int compareTo(UMTWebProvMessage o) {
    return this.getDate().compareTo(o.getDate());
  }

  public static class UMTWebProvMessageDateAdapter extends XmlAdapter<String, Date> {

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public UMTWebProvMessageDateAdapter() {
    }

    @Override
    public Date unmarshal(String v) throws Exception {
      return dateFormat.parse(v);
    }

    @Override
    public String marshal(Date v) throws Exception {
      return dateFormat.format(v);
    }
  }
}
