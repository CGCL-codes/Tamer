    public RatPoly differentiate() {
        if (deg == 0) return ZERO;
        RatPoly deriv = new RatPoly(BigRational.ZERO, deg - 1);
        for (int i = 0; i < deg; i++) deriv.coef[i] = coef[i + 1].times(new BigRational(i + 1));
        deriv.deg = deriv.degree();
        return deriv;
    }
