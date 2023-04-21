    protected int getInflectionPoint(double x, double y) {
        double X1 = getXCoord(getT1()), X2 = getXCoord(getT2()), Xc = (X1 + X2) / 2;
        double Y1 = getYCoord(getV1()), Y2 = getYCoord(getV2()), Yc = (Y1 + Y2) / 2;
        if (inflectionSet.get(TOP) && Math.abs(x - Xc) < 5 && Math.abs(y - Y1) < 5) return TOP;
        if (inflectionSet.get(TOP_LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Y1) < 5) return TOP_LEFT;
        if (inflectionSet.get(TOP_RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Y1) < 5) return TOP_RIGHT;
        if (inflectionSet.get(LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Yc) < 5) return LEFT;
        if (inflectionSet.get(RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Yc) < 5) return RIGHT;
        if (inflectionSet.get(BOTTOM) && Math.abs(x - Xc) < 5 && Math.abs(y - Y2) < 5) return BOTTOM;
        if (inflectionSet.get(BOTTOM_LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Y2) < 5) return BOTTOM_LEFT;
        if (inflectionSet.get(BOTTOM_RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Y2) < 5) return BOTTOM_RIGHT;
        return NONE;
    }
