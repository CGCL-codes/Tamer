package com.ohioedge.j2ee.api.org.proc.ejb;

import java.io.Serializable;
import org.j2eebuilder.util.LogManager;

/**
 * @(#)InputFactoryPK.java 1.350 01/12/03 Primary Key
 * @version 1.3.1
 * @since OEC1.2
 */
public class InputFactoryPK implements java.io.Serializable {

    public InputFactoryPK(Integer inputFactoryID) {
        this.inputFactoryID = inputFactoryID;
    }

    public InputFactoryPK() {
    }

    public String toString() {
        return String.valueOf(this.inputFactoryID);
    }

    public int hashCode() {
        return this.toString().hashCode();
    }

    public boolean equals(Object pk) {
        if (pk instanceof InputFactoryPK) return this.hashCode() == ((InputFactoryPK) pk).hashCode();
        return false;
    }

    public Integer inputFactoryID;

    public Integer getInputFactoryID() {
        return inputFactoryID;
    }

    public void setInputFactoryID(Integer inputFactoryID) {
        this.inputFactoryID = inputFactoryID;
    }
}
