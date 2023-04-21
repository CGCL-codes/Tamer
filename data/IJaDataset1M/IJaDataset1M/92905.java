package de.fhg.igd.earth.model.graph;

import java.io.Serializable;
import java.util.BitSet;

/**
 * This class represents a DistanceDetailGroup.
 * The distance values define the maximum distances from the projector center
 * where the child is visible. The FIRST entry in the distance field
 * correspondes to the SECOND child. The first child is visible if the
 * current distance is less than distance[0]. The second child is
 * visible if the current distance is less than distance[1] ...
 * At that moment only 2 Children are allowed !!!
 *
 * Title        : Earth
 * Copyright    : Copyright (c) 2001
 * Organisation : IGD FhG
 * @author       : Werner Beutel
 * @version      : 1.0
 */
public class DistanceDetailGroup extends SwitchGroup implements Serializable, Cloneable {

    /**
     * field of distances
     */
    private float distances_[];

    /*************************************************************************
     * Constructs and initializes a default DistanceDetailGroup.
     * Maximum of children is set to distances.length+1.
     * @param distances Field of distances
     ************************************************************************/
    public DistanceDetailGroup(float distances[]) {
        super();
        this.setWitchChild(CHILD_MASK);
        this.maxNumChildren_ = distances.length + 1;
        distances_ = distances;
    }

    /*************************************************************************
     * Returns the field of distances.
     * @return Field of distances
     ************************************************************************/
    public float[] getDistances() {
        return distances_;
    }

    /*************************************************************************
     * Sets the field of distances.
     * Released changes: CHANGE_UPDATE
     * @param distances Field of distances
     ************************************************************************/
    public void setDistances(float[] distances) {
        distances_ = distances;
        this.setChanges(CHANGE_Update);
    }

    /*************************************************************************
     * Returns TYPE_Group. Defined in ModelGraphObject.
     * @return type (always TYPE_Group)
     ************************************************************************/
    public int getType() {
        return TYPE_Group;
    }

    /*************************************************************************
     * Returns SUBTYPE_DistanceDetailGroup. Defined in ModelGraphObject
     * @return subType (always SUBTYPE_DistanceDetailGroup)
     ************************************************************************/
    public int getSubType() {
        return SUBTYPE_DistanceDetailGroup;
    }

    /*************************************************************************
     * Overwrites the group class method.
     * Every child can be added here.
     * @param n Node
     * @return true if child is allowed to be added
     ************************************************************************/
    protected boolean allowChildAddEvent(Node n) {
        return true;
    }

    /*************************************************************************
     * Returns the children mask for the given distance
     * @param distance Current distance from center
     * @return childMask
     ************************************************************************/
    public BitSet getChildMask(float distance) {
        BitSet childMask;
        childMask = new BitSet();
        if (distance < distances_[0]) childMask.set(0); else childMask.set(1);
        return childMask;
    }

    /*************************************************************************
     * Clones this DistanceDetailGroup
     ************************************************************************/
    public Object clone() {
        try {
            DistanceDetailGroup ddg = (DistanceDetailGroup) super.clone();
            return ddg;
        } catch (Exception e) {
            return null;
        }
    }
}
