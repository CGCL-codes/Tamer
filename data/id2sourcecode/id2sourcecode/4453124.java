    public double getQuantile(double p) {
        double x, x1, x2, error, q;
        int n, i;
        if (type == DISCRETE) {
            if (p <= 0) return domain.getLowerValue(); else if (p >= 1) return domain.getUpperValue(); else {
                n = domain.getSize();
                i = 0;
                x = domain.getValue(i);
                q = getDensity(x);
                while ((q < p) & (i < n)) {
                    i++;
                    x = domain.getValue(i);
                    q = q + getDensity(x);
                }
                return x;
            }
        } else {
            if (p <= 0) return domain.getLowerBound(); else if (p >= 1) return domain.getUpperBound(); else {
                x1 = domain.getLowerBound();
                x2 = domain.getUpperBound();
                x = (x1 + x2) / 2;
                q = getCDF(x);
                error = Math.abs(q - p);
                n = 1;
                while (error > 0.0001 & n < 100) {
                    n++;
                    if (q < p) x1 = x; else x2 = x;
                    x = (x1 + x2) / 2;
                    q = getCDF(x);
                    error = Math.abs(q - p);
                }
                return x;
            }
        }
    }
