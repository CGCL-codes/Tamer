    public void render(AtomShape atomShape1, int index1, AtomShape atomShape2, int index2, int order) {
        styleAtom1 = atomShape1.styleAtom;
        styleAtom2 = atomShape2.styleAtom;
        styleBond = atomShape1.styleBonds[index1];
        marBond = atomShape1.marBonds[index1];
        x1 = atomShape1.x;
        y1 = atomShape1.y;
        z1 = atomShape1.z;
        x2 = atomShape2.x;
        y2 = atomShape2.y;
        z2 = atomShape2.z;
        width1 = atomShape1.bondWidths[index1];
        width2 = atomShape2.bondWidths[index2];
        if (width1 < 4 && width2 < 4) {
            width1 = width2 = (width1 + width2) / 2;
        }
        color1 = atomShape1.colorBonds[index1];
        if (color1 == null) color1 = atomShape1.colorAtom;
        color2 = atomShape2.colorBonds[index2];
        if (color2 == null) color2 = atomShape2.colorAtom;
        sameColor = color1.equals(color2);
        if (!showAtoms) {
            diameter1 = diameter2 = 0;
        } else {
            diameter1 = (styleAtom1 == DisplayControl.NONE) ? 0 : atomShape1.diameter;
            diameter2 = (styleAtom2 == DisplayControl.NONE) ? 0 : atomShape2.diameter;
        }
        bondOrder = getRenderBondOrder(order);
        if (control.hasSelectionHalo(atomShape1.atom, index1)) renderHalo();
        if (styleBond != DisplayControl.NONE) renderBond();
    }
