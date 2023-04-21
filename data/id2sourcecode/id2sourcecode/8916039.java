    public double getDensity(double x) {
        int k = (int) Math.rint(x + 0.5), m = (k + steps) / 2;
        return comb(steps, m) * Math.pow(probability, m) * Math.pow(1 - probability, steps - m);
    }
