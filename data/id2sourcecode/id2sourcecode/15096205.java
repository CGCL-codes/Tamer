    public double getDensity(double x) {
        int k = (int) Math.rint(x), m = (k + steps) / 2;
        return Functions.comb(steps, m) * Math.pow(probability, m) * Math.pow(1 - probability, steps - m);
    }
