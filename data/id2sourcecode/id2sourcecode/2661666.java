    public void setMMandRadii(float mm, int outerRadius, int innerRadius, boolean forceRaster) {
        super.setMMandRadii(mm, outerRadius, innerRadius, forceRaster);
        a = (LR1 - LR2) / Math.PI;
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
    }
