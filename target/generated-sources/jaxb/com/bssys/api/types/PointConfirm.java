//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:42 PM MSK 
//


package com.bssys.api.types;

import java.math.BigInteger;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;


/**
 * Подтверждение выполнения операции над пунктом.
 * 
 * <p>Java class for point-confirm complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="point-confirm">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="point-id" use="required" type="{}integer-type" />
 *       &lt;attribute name="point-id-ext" type="{http://www.w3.org/2001/XMLSchema}anySimpleType" />
 *       &lt;attribute name="result-code" type="{http://www.w3.org/2001/XMLSchema}integer" />
 *       &lt;attribute name="error-text">
 *         &lt;simpleType>
 *           &lt;restriction base="{}text255-type">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "point-confirm")
public class PointConfirm {

    @XmlAttribute(name = "point-id", required = true)
    protected BigInteger pointId;
    @XmlAttribute(name = "point-id-ext")
    @XmlSchemaType(name = "anySimpleType")
    protected String pointIdExt;
    @XmlAttribute(name = "result-code")
    protected BigInteger resultCode;
    @XmlAttribute(name = "error-text")
    protected String errorText;

    /**
     * Gets the value of the pointId property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getPointId() {
        return pointId;
    }

    /**
     * Sets the value of the pointId property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setPointId(BigInteger value) {
        this.pointId = value;
    }

    /**
     * Gets the value of the pointIdExt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointIdExt() {
        return pointIdExt;
    }

    /**
     * Sets the value of the pointIdExt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointIdExt(String value) {
        this.pointIdExt = value;
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
     * Gets the value of the errorText property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorText() {
        return errorText;
    }

    /**
     * Sets the value of the errorText property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorText(String value) {
        this.errorText = value;
    }

}