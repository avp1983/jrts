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
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Представление денежного перевода при создании и отправлении.
 * 
 * <p>Java class for transfer-legal-archive complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="transfer-legal-archive">
 *   &lt;complexContent>
 *     &lt;extension base="{}transfer-legal">
 *       &lt;attribute name="checknumber" use="required" type="{}checknumber-type" />
 *       &lt;attribute name="fee" use="required" type="{}amount-type" />
 *       &lt;attribute name="creating-date" use="required" type="{}datetime-type" />
 *       &lt;attribute name="status-code" use="required" type="{}doc-status-type" />
 *       &lt;attribute name="status-text" use="required" type="{}text50-type" />
 *     &lt;/extension>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "transfer-legal-archive")
public class TransferLegalArchive
    extends TransferLegal
{

    @XmlAttribute(name = "checknumber", required = true)
    protected String checknumber;
    @XmlAttribute(name = "fee", required = true)
    protected BigDecimal fee;
    @XmlAttribute(name = "creating-date", required = true)
    protected XMLGregorianCalendar creatingDate;
    @XmlAttribute(name = "status-code", required = true)
    protected BigInteger statusCode;
    @XmlAttribute(name = "status-text", required = true)
    protected String statusText;

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
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setStatusCode(BigInteger value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the statusText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatusText() {
        return statusText;
    }

    /**
     * Sets the value of the statusText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatusText(String value) {
        this.statusText = value;
    }

}