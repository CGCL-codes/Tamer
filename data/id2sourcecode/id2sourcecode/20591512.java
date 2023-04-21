    public void render(AtomShape atomShape) {
        styleAtom = atomShape.styleAtom;
        atom = atomShape.atom;
        x = atomShape.x;
        y = atomShape.y;
        z = atomShape.z;
        diameter = atomShape.diameter;
        radius = (diameter + 1) / 2;
        xUpperLeft = x - radius;
        yUpperLeft = y - radius;
        color = atomShape.colorAtom;
        colorOutline = control.getColorAtomOutline(styleAtom, color);
        if (control.hasSelectionHalo(atom)) renderHalo();
        if (styleAtom != DisplayControl.NONE && styleAtom != DisplayControl.INVISIBLE) renderAtom();
    }
