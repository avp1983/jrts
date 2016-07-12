//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2016.07.07 at 02:19:42 PM MSK 
//


package com.bssys.api.types;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * Расписание работы.
 * 
 * <p>Java class for works complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="works">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence maxOccurs="unbounded" minOccurs="0">
 *         &lt;element name="data" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *                 &lt;attribute name="day-start">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{}integer-type">
 *                       &lt;pattern value="[1-7]{1}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="day-end">
 *                   &lt;simpleType>
 *                     &lt;restriction base="{}integer-type">
 *                       &lt;pattern value="[1-7]{1}"/>
 *                     &lt;/restriction>
 *                   &lt;/simpleType>
 *                 &lt;/attribute>
 *                 &lt;attribute name="worktime-start" type="{}time-type" />
 *                 &lt;attribute name="worktime-end" type="{}time-type" />
 *                 &lt;attribute name="breaktime-start" type="{}time-type" />
 *                 &lt;attribute name="breaktime-end" type="{}time-type" />
 *               &lt;/restriction>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "works", propOrder = {
    "data"
})
public class Works {

    protected List<Works.Data> data;

    /**
     * Gets the value of the data property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the data property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getData().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Works.Data }
     * 
     * 
     */
    public List<Works.Data> getData() {
        if (data == null) {
            data = new ArrayList<Works.Data>();
        }
        return this.data;
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
     *       &lt;attribute name="day-start">
     *         &lt;simpleType>
     *           &lt;restriction base="{}integer-type">
     *             &lt;pattern value="[1-7]{1}"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="day-end">
     *         &lt;simpleType>
     *           &lt;restriction base="{}integer-type">
     *             &lt;pattern value="[1-7]{1}"/>
     *           &lt;/restriction>
     *         &lt;/simpleType>
     *       &lt;/attribute>
     *       &lt;attribute name="worktime-start" type="{}time-type" />
     *       &lt;attribute name="worktime-end" type="{}time-type" />
     *       &lt;attribute name="breaktime-start" type="{}time-type" />
     *       &lt;attribute name="breaktime-end" type="{}time-type" />
     *     &lt;/restriction>
     *   &lt;/complexContent>
     * &lt;/complexType>
     * </pre>
     * 
     * 
     */
    @XmlAccessorType(XmlAccessType.FIELD)
    @XmlType(name = "")
    public static class Data {

        @XmlAttribute(name = "day-start")
        protected BigInteger dayStart;
        @XmlAttribute(name = "day-end")
        protected BigInteger dayEnd;
        @XmlAttribute(name = "worktime-start")
        protected XMLGregorianCalendar worktimeStart;
        @XmlAttribute(name = "worktime-end")
        protected XMLGregorianCalendar worktimeEnd;
        @XmlAttribute(name = "breaktime-start")
        protected XMLGregorianCalendar breaktimeStart;
        @XmlAttribute(name = "breaktime-end")
        protected XMLGregorianCalendar breaktimeEnd;

        /**
         * Gets the value of the dayStart property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDayStart() {
            return dayStart;
        }

        /**
         * Sets the value of the dayStart property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDayStart(BigInteger value) {
            this.dayStart = value;
        }

        /**
         * Gets the value of the dayEnd property.
         * 
         * @return
         *     possible object is
         *     {@link BigInteger }
         *     
         */
        public BigInteger getDayEnd() {
            return dayEnd;
        }

        /**
         * Sets the value of the dayEnd property.
         * 
         * @param value
         *     allowed object is
         *     {@link BigInteger }
         *     
         */
        public void setDayEnd(BigInteger value) {
            this.dayEnd = value;
        }

        /**
         * Gets the value of the worktimeStart property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getWorktimeStart() {
            return worktimeStart;
        }

        /**
         * Sets the value of the worktimeStart property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setWorktimeStart(XMLGregorianCalendar value) {
            this.worktimeStart = value;
        }

        /**
         * Gets the value of the worktimeEnd property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getWorktimeEnd() {
            return worktimeEnd;
        }

        /**
         * Sets the value of the worktimeEnd property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setWorktimeEnd(XMLGregorianCalendar value) {
            this.worktimeEnd = value;
        }

        /**
         * Gets the value of the breaktimeStart property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getBreaktimeStart() {
            return breaktimeStart;
        }

        /**
         * Sets the value of the breaktimeStart property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setBreaktimeStart(XMLGregorianCalendar value) {
            this.breaktimeStart = value;
        }

        /**
         * Gets the value of the breaktimeEnd property.
         * 
         * @return
         *     possible object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public XMLGregorianCalendar getBreaktimeEnd() {
            return breaktimeEnd;
        }

        /**
         * Sets the value of the breaktimeEnd property.
         * 
         * @param value
         *     allowed object is
         *     {@link XMLGregorianCalendar }
         *     
         */
        public void setBreaktimeEnd(XMLGregorianCalendar value) {
            this.breaktimeEnd = value;
        }

    }

}