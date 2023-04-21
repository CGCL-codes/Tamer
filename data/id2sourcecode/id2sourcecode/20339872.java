    public void setMMandRadii(float mm, int outerRadius, int innerRadius, boolean forceRaster) {
        super.setMMandRadii(mm, outerRadius, innerRadius, forceRaster);
        a = Math.PI / (LR1 - LR2);
        b = -Math.PI * LR2 / (LR1 - LR2);
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
    }
