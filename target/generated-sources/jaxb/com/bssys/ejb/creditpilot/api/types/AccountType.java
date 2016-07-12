//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:40 PM MSK 
//


package com.bssys.ejb.creditpilot.api.types;

import java.math.BigDecimal;
import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * Ответ на запрос ACCOUNT
 * 
 * <p>Java class for accountType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="accountType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;all>
 *         &lt;element name="balance" type="{}amountType"/>
 *         &lt;element name="paylimit" type="{}amountType"/>
 *         &lt;element name="today">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="goodsum" use="required" type="{}amountType" />
 *                 &lt;attribute name="goodcount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/all>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "accountType", propOrder = {

})
public class AccountType {

    @XmlElement(required = true)
    protected BigDecimal balance;
    @XmlElement(required = true)
    protected BigDecimal paylimit;
    @XmlElement(required = true)
    protected AccountType.Today today;

    /**
     * Gets the value of the balance property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getBalance() {
        return balance;
    }

    /**
     * Sets the value of the balance property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setBalance(BigDecimal value) {
        this.balance = value;
    }

    /**
     * Gets the value of the paylimit property.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPaylimit() {
        return paylimit;
    }

    /**
     * Sets the value of the paylimit property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPaylimit(BigDecimal value) {
        this.paylimit = value;
    }

    /**
     * Gets the value of the today property.
     * 
     * @return
     *     possible object is
     *     {@link AccountType.Today }
     *     
     */
    public AccountType.Today getToday() {
        return today;
    }

    /**
     * Sets the value of the today property.
     * 
     * @param value
     *     allowed object is
     *     {@link AccountType.Today }
     *     
     */
    public void setToday(AccountType.Today value) {
        this.today = value;
    }


    /**
     * <p>Java class for anonymous complex type.
     * 
     * <p>The following schema fragment specifies the expected content contained within this class.
     * 
     * <pre>
     * &lt;complexType>
     *   &lt;complexContent>
     *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
     *       &lt;attribute name="goodsum" use="required" type="{}amountType" />
     *       &lt;attribute name="goodcount" use="required" type="{http://www.w3.org/2001/XMLSchema}integer" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Today {

        @XmlAttribute(name = "goodsum", required = true)
        protected BigDecimal goodsum;
        @XmlAttribute(name = "goodcount", required = true)
        protected BigInteger goodcount;

        /**
         * Gets the value of the goodsum property.
         * 
         * @return
         *     possible object is
         *     {@link BigDecimal }
         *     
         */
        public BigDecimal getGoodsum() {
            return goodsum;
        }

        /**
         * Sets the value of the goodsum property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigDecimal }
         *     
         */
        public void setGoodsum(BigDecimal value) {
            this.goodsum = value;
        }

        /**
         * Gets the value of the goodcount property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getGoodcount() {
            return goodcount;
        }

        /**
         * Sets the value of the goodcount property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setGoodcount(BigInteger value) {
            this.goodcount = value;
        }

    }

}