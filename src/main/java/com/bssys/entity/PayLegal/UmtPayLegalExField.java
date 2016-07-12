package com.bssys.entity.PayLegal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "REC")
public class UmtPayLegalExField {
  @XmlElement(name = "ExName")
  private String exName;
  @XmlElement(name = "ExValue")
  private String exValue;
  @XmlElement(name = "ExTitle")
  private String exTitle;
  @XmlElement(name = "IsEditable")
  private Integer isEditable;
  @XmlElement(name = "ChoiceValuesXML")
  private UmtPayLegalExFieldParams choiceValuesXML;
  @XmlElement(name = "Step")
  private Integer step;
  @XmlElement(name = "Description")
  private String description;

  public String getExName() {
    return exName;
  }

  public String getExValue() {
    return exValue;
  }

  public UmtPayLegalExFieldParams getChoiceValuesXML() {
    return choiceValuesXML;
  }

  public void setChoiceValuesXML(UmtPayLegalExFieldParams choiceValuesXML) {
    this.choiceValuesXML = choiceValuesXML;
  }

  public String getExTitle() {
    return exTitle;
  }

  public Integer getIsEditable() {
    return isEditable;
  }

  public Integer getStep() {
    return step;
  }

  public void setExValue(String exValue) {
    this.exValue = exValue;
  }

  public String getDescription() {
    return description;
  }

  @SuppressWarnings("UnusedDeclaration")
  public UmtPayLegalExField() {
  }

  public void setExName(String exName) {
    this.exName = exName;
  }

  public void setExTitle(String exTitle) {
    this.exTitle = exTitle;
  }

  public void setIsEditable(Integer isEditable) {
    this.isEditable = isEditable;
  }

  public void setStep(Integer step) {
    this.step = step;
  }

  public void setDescription(String description) {
    this.description = description;
  }
}


