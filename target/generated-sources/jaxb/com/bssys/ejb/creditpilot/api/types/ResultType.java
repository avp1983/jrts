//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:40 PM MSK 
//


package com.bssys.ejb.creditpilot.api.types;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Результат выполнения запроса
 * 
 * <p>Java class for resultType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="resultType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="fatal" use="required" type="{http://www.w3.org/2001/XMLSchema}boolean" />
 *       &lt;attribute name="resultCode" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="resultDescription" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="providerResultMessage" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "resultType")
public class ResultType {

    @XmlAttribute(name = "fatal", required = true)
    protected boolean fatal;
    @XmlAttribute(name = "resultCode", required = true)
    protected BigInteger resultCode;
    @XmlAttribute(name = "resultDescription", required = true)
    protected String resultDescription;
    @XmlAttribute(name = "providerResultMessage", required = true)
    protected String providerResultMessage;

    /**
     * Gets the value of the fatal property.
     * 
     */
    public boolean isFatal() {
        return fatal;
    }

    /**
     * Sets the value of the fatal property.
     * 
     */
    public void setFatal(boolean value) {
        this.fatal = value;
    }

    /**
     * Gets the value of the resultCode property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getResultCode() {
        return resultCode;
    }

    /**
     * Sets the value of the resultCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setResultCode(BigInteger value) {
        this.resultCode = value;
    }

    /**
     * Gets the value of the resultDescription property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getResultDescription() {
        return resultDescription;
    }

    /**
     * Sets the value of the resultDescription property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setResultDescription(String value) {
        this.resultDescription = value;
    }

    /**
     * Gets the value of the providerResultMessage property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProviderResultMessage() {
        return providerResultMessage;
    }

    /**
     * Sets the value of the providerResultMessage property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProviderResultMessage(String value) {
        this.providerResultMessage = value;
    }

}
