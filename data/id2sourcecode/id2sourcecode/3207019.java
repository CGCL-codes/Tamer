    public void setParameters(double l, double r, double m, double p) {
        double lower, upper, step;
        if (l >= r) r = l + 1;
        if (l >= m || m >= r) m = (l + r) / 2;
        if (p <= 0) p = 1;
        left = l;
        right = r;
        med = m;
        power = p;
        c = power / (right - left);
        super.setParameters(left, right, 0.01 * (right - left), CONTINUOUS);
    }
