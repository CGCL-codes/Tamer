    public double getNullValue() {
        double x = 0;
        for (int i = 0; i < iterationCount; i++) {
            x = (x1 + x2) / 2;
            if (f(x) >= 0) {
                x2 = x;
            } else {
                x1 = x;
            }
            log.debug("x = " + x + ",   f(x1) = " + f(x1));
        }
        return x;
    }
