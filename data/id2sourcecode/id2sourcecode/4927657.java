    static Vektor teiler(long X) throws JasymcaException {
        Vector teiler = new Vector();
        while (X % 2L == 0) {
            teiler.addElement(Zahl.TWO);
            X /= 2L;
        }
        while (X % 3L == 0) {
            teiler.addElement(Zahl.THREE);
            X /= 3L;
        }
        while (X % 5L == 0) {
            teiler.addElement(new Unexakt(5.0));
            X /= 5L;
        }
        long f = 7L;
        while (X != 1L) {
            f = kleinsterTeiler(X, f);
            if (f < 0) return null;
            teiler.addElement(new Exakt(f, 1L));
            X /= f;
        }
        return Vektor.create(teiler);
    }
