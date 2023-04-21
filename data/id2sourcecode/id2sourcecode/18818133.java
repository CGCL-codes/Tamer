    public void fillScreenedCircleCentered(short colixFill, int diameter, int x, int y, int z) {
        if (isClippedZ(z)) return;
        int r = (diameter + 1) / 2;
        boolean isClipped = x < r || x + r >= width || y < r || y + r >= height;
        if (setColix(getColixTranslucent(colixFill, false, 0))) {
            if (!isClipped) circle3d.plotCircleCenteredUnclipped(x, y, z, diameter); else if (!isClippedXY(diameter, x, y)) circle3d.plotCircleCenteredClipped(x, y, z, diameter);
        }
        if (!setColix(getColixTranslucent(colixFill, true, 0.5f))) return;
        if (!isClipped) circle3d.plotFilledCircleCenteredUnclipped(x, y, z, diameter); else if (!isClippedXY(diameter, x, y)) circle3d.plotFilledCircleCenteredClipped(x, y, z, diameter);
    }
