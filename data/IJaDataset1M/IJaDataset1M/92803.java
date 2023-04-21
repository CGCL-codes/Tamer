package de.ibis.permoto.model.basic.applicationmodel;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * <p>Java class for anonymous complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;attribute name="defaultNrParallelWorkers" type="{http://www.w3.org/2001/XMLSchema}int" default="1" />
 *       &lt;attribute name="defaultQueueLength" type="{http://www.w3.org/2001/XMLSchema}int" default="-1" />
 *       &lt;attribute name="defaultServiceTime" type="{http://www.w3.org/2001/XMLSchema}float" default="0.0" />
 *       &lt;attribute name="identifier" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="isDiffStation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="isEndStation" type="{http://www.w3.org/2001/XMLSchema}boolean" default="false" />
 *       &lt;attribute name="realName" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 *       &lt;attribute name="saveName" type="{http://www.w3.org/2001/XMLSchema}string" default="undefined" />
 *       &lt;attribute name="staticDelayTime" type="{http://www.w3.org/2001/XMLSchema}float" default="0.0" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "Item")
public class Item {

    @XmlAttribute
    protected Integer defaultNrParallelWorkers;

    @XmlAttribute
    protected Integer defaultQueueLength;

    @XmlAttribute
    protected Float defaultServiceTime;

    @XmlAttribute(required = true)
    protected String identifier;

    @XmlAttribute
    protected Boolean isDiffStation;

    @XmlAttribute
    protected Boolean isEndStation;

    @XmlAttribute(required = true)
    protected String realName;

    @XmlAttribute
    protected String saveName;

    @XmlAttribute
    protected Float staticDelayTime;

    /**
     * Gets the value of the defaultNrParallelWorkers property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getDefaultNrParallelWorkers() {
        if (defaultNrParallelWorkers == null) {
            return 1;
        } else {
            return defaultNrParallelWorkers;
        }
    }

    /**
     * Sets the value of the defaultNrParallelWorkers property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDefaultNrParallelWorkers(Integer value) {
        this.defaultNrParallelWorkers = value;
    }

    /**
     * Gets the value of the defaultQueueLength property.
     * 
     * @return
     *     possible object is
     *     {@link Integer }
     *     
     */
    public int getDefaultQueueLength() {
        if (defaultQueueLength == null) {
            return -1;
        } else {
            return defaultQueueLength;
        }
    }

    /**
     * Sets the value of the defaultQueueLength property.
     * 
     * @param value
     *     allowed object is
     *     {@link Integer }
     *     
     */
    public void setDefaultQueueLength(Integer value) {
        this.defaultQueueLength = value;
    }

    /**
     * Gets the value of the defaultServiceTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public float getDefaultServiceTime() {
        if (defaultServiceTime == null) {
            return 0.0F;
        } else {
            return defaultServiceTime;
        }
    }

    /**
     * Sets the value of the defaultServiceTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setDefaultServiceTime(Float value) {
        this.defaultServiceTime = value;
    }

    /**
     * Gets the value of the identifier property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * Sets the value of the identifier property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setIdentifier(String value) {
        this.identifier = value;
    }

    /**
     * Gets the value of the isDiffStation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsDiffStation() {
        if (isDiffStation == null) {
            return false;
        } else {
            return isDiffStation;
        }
    }

    /**
     * Sets the value of the isDiffStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsDiffStation(Boolean value) {
        this.isDiffStation = value;
    }

    /**
     * Gets the value of the isEndStation property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public boolean isIsEndStation() {
        if (isEndStation == null) {
            return false;
        } else {
            return isEndStation;
        }
    }

    /**
     * Sets the value of the isEndStation property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setIsEndStation(Boolean value) {
        this.isEndStation = value;
    }

    /**
     * Gets the value of the realName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getRealName() {
        return realName;
    }

    /**
     * Sets the value of the realName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setRealName(String value) {
        this.realName = value;
    }

    /**
     * Gets the value of the saveName property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSaveName() {
        if (saveName == null) {
            return "undefined";
        } else {
            return saveName;
        }
    }

    /**
     * Sets the value of the saveName property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSaveName(String value) {
        this.saveName = value;
    }

    /**
     * Gets the value of the staticDelayTime property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public float getStaticDelayTime() {
        if (staticDelayTime == null) {
            return 0.0F;
        } else {
            return staticDelayTime;
        }
    }

    /**
     * Sets the value of the staticDelayTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setStaticDelayTime(Float value) {
        this.staticDelayTime = value;
    }
}
