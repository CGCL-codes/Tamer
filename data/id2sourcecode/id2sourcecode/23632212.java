    public Qernal getQernal(RebinDescriptor ddx, RebinDescriptor ddy, Datum xTagWidth, Datum yTagWidth) {
        Datum d = ddx.binCenter(0);
        int i = ddx.whichBin(d.add(xTagWidth).doubleValue(d.getUnits()), d.getUnits());
        int dx0 = i / 2;
        int dx1 = i / 2;
        int dy0, dy1;
        if (UnitsUtil.isRatiometric(yTagWidth.getUnits())) {
            if (!ddy.isLog()) throw new IllegalArgumentException("need log axis");
            d = ddy.binCenter(0);
            double f = yTagWidth.doubleValue(Units.log10Ratio);
            i = ddy.whichBin(d.multiply(Math.pow(10, f)).doubleValue(d.getUnits()), d.getUnits());
            dy0 = i / 2;
            dy1 = (i + 1) / 2;
        } else {
            d = ddy.binCenter(0);
            i = ddy.whichBin(d.add(yTagWidth).doubleValue(d.getUnits()), d.getUnits());
            dy0 = i / 2;
            dy1 = (i + 1) / 2;
        }
        if (dx0 == 0 && dx1 == 0 && dy0 == 0 && dy1 == 0) {
            return new NNQernalOne(ddx.numberOfBins(), ddy.numberOfBins());
        } else {
            return new NNQernal(dx0, dx1, dy0, dy1, ddx.numberOfBins(), ddy.numberOfBins());
        }
    }
