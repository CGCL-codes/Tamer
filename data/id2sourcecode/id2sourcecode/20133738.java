    public XGaussianLens(float mm, float tc, float tf, int outerRadius, int innerRadius, int x, int y) {
        super(mm, tc, tf, outerRadius, innerRadius, x, y);
        a = Math.PI / (LR1 - LR2);
        b = -Math.PI * LR2 / (LR1 - LR2);
        c = (MM - 1) / 2;
        e = (1 + MM) / 2;
    }
