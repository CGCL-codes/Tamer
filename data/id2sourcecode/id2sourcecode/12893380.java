    public double getDensity(double x) {
        int k = (int) Math.rint(x), m;
        if ((k + steps) % 2 == 0) m = (k + steps) / 2; else m = (k + steps + 1) / 2;
        return comb(steps, m) / Math.pow(2, steps);
    }
