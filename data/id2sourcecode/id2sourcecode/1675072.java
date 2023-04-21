    public FSGaussianLens(float mm, int outerRadius, int innerRadius, int x, int y) {
        this.MM = mm;
        this.LR1 = outerRadius;
        this.LR2 = innerRadius;
        updateMagBufferWorkingDimensions();
        a = Math.PI / (LR1 - LR2);
        b = -Math.PI * LR2 / (LR1 - LR2);
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
        lx = x;
        ly = y;
    }
