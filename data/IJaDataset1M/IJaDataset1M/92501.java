package com.idedeluxe.bc.core.model;

import com.idedeluxe.bc.core.model.struc.Structure;

public class MultiArrayPlaceLeaf extends Leaf implements MultiArrayPlaceElement {

    private Structure[] structure = new Structure[0];

    @Override
    public Structure[] getStructure() {
        return structure;
    }

    @Override
    public int getArrayPlaceCount() {
        return 2;
    }
}
