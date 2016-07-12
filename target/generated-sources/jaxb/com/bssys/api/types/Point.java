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
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Данные пункта отправки / выдачи переводов.
 * 
 * <p>Java class for point complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="point">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="location" type="{}location-type"/>
 *         &lt;element name="contact" type="{}contact-type" minOccurs="0"/>
 *         &lt;element name="works" type="{}works" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attribute name="point-id" type="{}integer-type" />
 *       &lt;attribute name="link-point" use="required" type="{}integer-type" />
 *       &lt;attribute name="point-name-rus" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{}text255-type">
 *             &lt;minLength value="1"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="point-name-int" use="required">
 *         &lt;simpleType>
 *           &lt;restriction base="{}text255-type">
 *             &lt;minLength value="1"/>
 *             &lt;pattern value="\P{IsCyrillic}*"/>
 *           &lt;/restriction>
 *         &lt;/simpleType>
 *       &lt;/attribute>
 *       &lt;attribute name="enabled" type="{}integer-type" />
 *       &lt;attribute name="enableddate" type="{}datetime-type" />
 *       &lt;attribute name="point-id-ext" type="{}text150-type" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "point", propOrder = {
    "location",
    "contact",
    "works"
})
public class Point {

    @XmlElement(required = true)
    protected LocationType location;
    protected ContactType contact;
    protected Works works;
    @XmlAttribute(name = "point-id")
    protected BigInteger pointId;
    @XmlAttribute(name = "link-point", required = true)
    protected BigInteger linkPoint;
    @XmlAttribute(name = "point-name-rus", required = true)
    protected String pointNameRus;
    @XmlAttribute(name = "point-name-int", required = true)
    protected String pointNameInt;
    @XmlAttribute(name = "enabled")
    protected BigInteger enabled;
    @XmlAttribute(name = "enableddate")
    protected XMLGregorianCalendar enableddate;
    @XmlAttribute(name = "point-id-ext")
    protected String pointIdExt;

    /**
     * Gets the value of the location property.
     * 
     * @return
     *     possible object is
     *     {@link LocationType }
     *     
     */
    public LocationType getLocation() {
        return location;
    }

    /**
     * Sets the value of the location property.
     * 
     * @param value
     *     allowed object is
     *     {@link LocationType }
     *     
     */
    public void setLocation(LocationType value) {
        this.location = value;
    }

    /**
     * Gets the value of the contact property.
     * 
     * @return
     *     possible object is
     *     {@link ContactType }
     *     
     */
    public ContactType getContact() {
        return contact;
    }

    /**
     * Sets the value of the contact property.
     * 
     * @param value
     *     allowed object is
     *     {@link ContactType }
     *     
     */
    public void setContact(ContactType value) {
        this.contact = value;
    }

    /**
     * Gets the value of the works property.
     * 
     * @return
     *     possible object is
     *     {@link Works }
     *     
     */
    public Works getWorks() {
        return works;
    }

    /**
     * Sets the value of the works property.
     * 
     * @param value
     *     allowed object is
     *     {@link Works }
     *     
     */
    public void setWorks(Works value) {
        this.works = value;
    }

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
     * Gets the value of the linkPoint property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getLinkPoint() {
        return linkPoint;
    }

    /**
     * Sets the value of the linkPoint property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setLinkPoint(BigInteger value) {
        this.linkPoint = value;
    }

    /**
     * Gets the value of the pointNameRus property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointNameRus() {
        return pointNameRus;
    }

    /**
     * Sets the value of the pointNameRus property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointNameRus(String value) {
        this.pointNameRus = value;
    }

    /**
     * Gets the value of the pointNameInt property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPointNameInt() {
        return pointNameInt;
    }

    /**
     * Sets the value of the pointNameInt property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPointNameInt(String value) {
        this.pointNameInt = value;
    }

    /**
     * Gets the value of the enabled property.
     * 
     * @return
     *     possible object is
     *     {@link BigInteger }
     *     
     */
    public BigInteger getEnabled() {
        return enabled;
    }

    /**
     * Sets the value of the enabled property.
     * 
     * @param value
     *     allowed object is
     *     {@link BigInteger }
     *     
     */
    public void setEnabled(BigInteger value) {
        this.enabled = value;
    }

    /**
     * Gets the value of the enableddate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getEnableddate() {
        return enableddate;
    }

    /**
     * Sets the value of the enableddate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setEnableddate(XMLGregorianCalendar value) {
        this.enableddate = value;
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

}
