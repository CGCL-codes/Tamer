    public FuzzyNumber calc(FuzzyNumber af, FuzzyNumber bf) {
        double b[] = bf.data(), a[] = af.data();
        double c[] = new double[b.length];
        double atmp[] = new double[2], btmp[] = new double[2];
        for (int i = 0; i < b.length; i += 3) {
            atmp[0] = a[i];
            atmp[1] = a[i + 1];
            btmp[0] = b[i];
            btmp[1] = b[i + 1];
            double res[] = div(atmp, btmp);
            c[i] = res[0];
            c[i + 1] = res[1];
            c[i + 2] = a[i + 2];
        }
        return new DiscreteNumber(c);
    }
