    public FSScramblingLens(float mm, int outerRadius, int innerRadius, int x, int y) {
        this.MM = mm;
        this.LR1 = outerRadius;
        this.LR2 = innerRadius;
        updateMagBufferWorkingDimensions();
        a = (LR1 - LR2) / Math.PI;
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
        lx = x;
        ly = y;
    }
