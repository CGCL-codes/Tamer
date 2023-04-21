    private Number buildDifferenceTable() {
        difference[0][1] = numerical[0][1].copy().minus(symbolic[0][1]);
        difference[0][0] = numerical[0][0];
        Function abs = new AbsFunction();
        Number norm = abs.calc(difference[0][1]);
        for (int i = 1; i < pointCount; i++) {
            difference[i][1] = numerical[i][1].copy().minus(symbolic[i][1]);
            difference[i][0] = numerical[i][0];
            Number mod = abs.calc(difference[i][1]);
            if (norm.compareTo(mod) < 0) norm = mod;
        }
        return norm;
    }
