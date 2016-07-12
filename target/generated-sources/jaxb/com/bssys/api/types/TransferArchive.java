//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:42 PM MSK 
//


package com.bssys.api.types;

import java.math.BigDecimal;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Наиболее полное представление денежного перевода.
 * 
 * <p>Java class for transfer-archive complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transfer-archive">
 *   &lt;complexContent>
 *     &lt;extension base="{}transfer">
 *       &lt;attribute name="checknumber" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{}checknumber-type">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="creating-date" use="required" type="{}datetime-type" />
 *       &lt;attribute name="processing-date" type="{}datetime-type" />
 *       &lt;attribute name="fee" type="{}amount-type" />
 *       &lt;attribute name="amount-charge" type="{}amount-type" />
 *       &lt;attribute name="currency-charge" type="{}alpha3-type" />
 *       &lt;attribute name="rate" type="{http://www.w3.org/2001/XMLSchema}double" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfer-archive")
public class TransferArchive
    extends Transfer
{

    @XmlAttribute(name = "checknumber", required = true)
    protected String checknumber;
    @XmlAttribute(name = "creating-date", required = true)
    protected XMLGregorianCalendar creatingDate;
    @XmlAttribute(name = "processing-date")
    protected XMLGregorianCalendar processingDate;
    @XmlAttribute(name = "fee")
    protected BigDecimal fee;
    @XmlAttribute(name = "amount-charge")
    protected BigDecimal amountCharge;
    @XmlAttribute(name = "currency-charge")
    protected String currencyCharge;
    @XmlAttribute(name = "rate")
    protected Double rate;

    /**
     * Gets the value of the checknumber property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getChecknumber() {
        return checknumber;
    }

    /**
     * Sets the value of the checknumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setChecknumber(String value) {
        this.checknumber = value;
    }

    /**
     * Gets the value of the creatingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getCreatingDate() {
        return creatingDate;
    }

    /**
     * Sets the value of the creatingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setCreatingDate(XMLGregorianCalendar value) {
        this.creatingDate = value;
    }

    /**
     * Gets the value of the processingDate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getProcessingDate() {
        return processingDate;
    }

    /**
     * Sets the value of the processingDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setProcessingDate(XMLGregorianCalendar value) {
        this.processingDate = value;
    }

    /**
     * Gets the value of the fee property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getFee() {
        return fee;
    }

    /**
     * Sets the value of the fee property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setFee(BigDecimal value) {
        this.fee = value;
    }

    /**
     * Gets the value of the amountCharge property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmountCharge() {
        return amountCharge;
    }

    /**
     * Sets the value of the amountCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmountCharge(BigDecimal value) {
        this.amountCharge = value;
    }

    /**
     * Gets the value of the currencyCharge property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyCharge() {
        return currencyCharge;
    }

    /**
     * Sets the value of the currencyCharge property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyCharge(String value) {
        this.currencyCharge = value;
    }

    /**
     * Gets the value of the rate property.
     * 
     * @return
     *     possible object is
     *     {@link Double }
     *     
     */
    public Double getRate() {
        return rate;
    }

    /**
     * Sets the value of the rate property.
     * 
     * @param value
     *     allowed object is
     *     {@link Double }
     *     
     */
    public void setRate(Double value) {
        this.rate = value;
    }

}