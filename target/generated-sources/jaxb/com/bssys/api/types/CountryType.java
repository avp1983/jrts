//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:42 PM MSK 
//


package com.bssys.api.types;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;


/**
 * Числовой ISO-код страны.
 * 
 * <p>Java class for country-type complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="country-type">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="numeric" use="required" type="{}numeric3-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "country-type")
public class CountryType {

    @XmlAttribute(name = "numeric", required = true)
    protected String numeric;

    /**
     * Gets the value of the numeric property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNumeric() {
        return numeric;
    }

    /**
     * Sets the value of the numeric property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNumeric(String value) {
        this.numeric = value;
    }

}