    public void fillScreenedCircleCentered(short colixFill, int diameter, int x, int y, int z) {
        if (diameter == 0 || z < slab || z > depth) return;
        int r = (diameter + 1) / 2;
        setColix(colixFill);
        isTranslucent = true;
        if (x >= r && x + r < width && y >= r && y + r < height) {
            circle3d.plotFilledCircleCenteredUnclipped(x, y, z, diameter);
            isTranslucent = false;
            circle3d.plotCircleCenteredUnclipped(x, y, z, diameter);
        } else {
            circle3d.plotFilledCircleCenteredClipped(x, y, z, diameter);
            isTranslucent = false;
            circle3d.plotCircleCenteredClipped(x, y, z, diameter);
        }
    }
