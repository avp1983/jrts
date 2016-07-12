//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:42 PM MSK 
//


package com.bssys.api.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;


/**
 * Основные параметры денежного перевода.
 * 
 * <p>Java class for transfer complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transfer">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="sender" type="{}client-type" minOccurs="0"/>
 *         &lt;element name="receiver" type="{}client-type" minOccurs="0"/>
 *         &lt;element name="issue" type="{}issue-type" minOccurs="0"/>
 *       &lt;/all>
 *       &lt;attribute name="number" use="required" type="{}integer-type" />
 *       &lt;attribute name="amount" use="required" type="{}amount-type" />
 *       &lt;attribute name="currency-alpha" use="required" type="{}alpha3-type" />
 *       &lt;attribute name="note" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{}text50-type">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="service-note" type="{}text255-type" />
 *       &lt;attribute name="smstosender" type="{}int01-type" />
 *       &lt;attribute name="smstoreceiver" type="{}int01-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfer", propOrder = {

})
@XmlSeeAlso({
    TransferEdit.class,
    TransferSend.class,
    TransferAddress.class,
    TransferSearchReply.class,
    TransferArchive.class
})
public class Transfer {

    protected ClientType sender;
    protected ClientType receiver;
    protected IssueType issue;
    @XmlAttribute(name = "number", required = true)
    protected BigInteger number;
    @XmlAttribute(name = "amount", required = true)
    protected BigDecimal amount;
    @XmlAttribute(name = "currency-alpha", required = true)
    protected String currencyAlpha;
    @XmlAttribute(name = "note", required = true)
    protected String note;
    @XmlAttribute(name = "service-note")
    protected String serviceNote;
    @XmlAttribute(name = "smstosender")
    protected Integer smstosender;
    @XmlAttribute(name = "smstoreceiver")
    protected Integer smstoreceiver;

    /**
     * Gets the value of the sender property.
     * 
     * @return
     *     possible object is
     *     {@link ClientType }
     *     
     */
    public ClientType getSender() {
        return sender;
    }

    /**
     * Sets the value of the sender property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientType }
     *     
     */
    public void setSender(ClientType value) {
        this.sender = value;
    }

    /**
     * Gets the value of the receiver property.
     * 
     * @return
     *     possible object is
     *     {@link ClientType }
     *     
     */
    public ClientType getReceiver() {
        return receiver;
    }

    /**
     * Sets the value of the receiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link ClientType }
     *     
     */
    public void setReceiver(ClientType value) {
        this.receiver = value;
    }

    /**
     * Gets the value of the issue property.
     * 
     * @return
     *     possible object is
     *     {@link IssueType }
     *     
     */
    public IssueType getIssue() {
        return issue;
    }

    /**
     * Sets the value of the issue property.
     * 
     * @param value
     *     allowed object is
     *     {@link IssueType }
     *     
     */
    public void setIssue(IssueType value) {
        this.issue = value;
    }

    /**
     * Gets the value of the number property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getNumber() {
        return number;
    }

    /**
     * Sets the value of the number property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setNumber(BigInteger value) {
        this.number = value;
    }

    /**
     * Gets the value of the amount property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Sets the value of the amount property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setAmount(BigDecimal value) {
        this.amount = value;
    }

    /**
     * Gets the value of the currencyAlpha property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCurrencyAlpha() {
        return currencyAlpha;
    }

    /**
     * Sets the value of the currencyAlpha property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCurrencyAlpha(String value) {
        this.currencyAlpha = value;
    }

    /**
     * Gets the value of the note property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets the value of the note property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNote(String value) {
        this.note = value;
    }

    /**
     * Gets the value of the serviceNote property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getServiceNote() {
        return serviceNote;
    }

    /**
     * Sets the value of the serviceNote property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setServiceNote(String value) {
        this.serviceNote = value;
    }

    /**
     * Gets the value of the smstosender property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSmstosender() {
        return smstosender;
    }

    /**
     * Sets the value of the smstosender property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSmstosender(Integer value) {
        this.smstosender = value;
    }

    /**
     * Gets the value of the smstoreceiver property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public Integer getSmstoreceiver() {
        return smstoreceiver;
    }

    /**
     * Sets the value of the smstoreceiver property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setSmstoreceiver(Integer value) {
        this.smstoreceiver = value;
    }

}