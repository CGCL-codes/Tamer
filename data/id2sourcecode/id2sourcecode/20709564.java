    protected int getActionPoint(double x, double y) {
        double X1 = getXFromTime(getT1()), X2 = getXFromTime(getT2()), Xc = (X1 + X2) / 2;
        double Y1 = getYFromValue(getV1()), Y2 = getYFromValue(getV2()), Yc = (Y1 + Y2) / 2;
        if (actionSet.get(TOP) && Math.abs(x - Xc) < 5 && Math.abs(y - Y1) < 5) return TOP;
        if (actionSet.get(TOP_LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Y1) < 5) return TOP_LEFT;
        if (actionSet.get(TOP_RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Y1) < 5) return TOP_RIGHT;
        if (actionSet.get(LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Yc) < 5) return LEFT;
        if (actionSet.get(RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Yc) < 5) return RIGHT;
        if (actionSet.get(BOTTOM) && Math.abs(x - Xc) < 5 && Math.abs(y - Y2) < 5) return BOTTOM;
        if (actionSet.get(BOTTOM_LEFT) && Math.abs(x - X1) < 5 && Math.abs(y - Y2) < 5) return BOTTOM_LEFT;
        if (actionSet.get(BOTTOM_RIGHT) && Math.abs(x - X2) < 5 && Math.abs(y - Y2) < 5) return BOTTOM_RIGHT;
        return NONE;
    }
