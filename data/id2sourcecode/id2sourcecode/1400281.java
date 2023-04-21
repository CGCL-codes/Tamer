    public double evaluate(double x) {
        int n1 = 0;
        int n2 = xd.length - 1;
        if (n2 < 1) {
            return yd[0];
        }
        if (n2 == 1) {
            return Interpolation.linear(x, xd[0], xd[1], yd[0], yd[1]);
        }
        if (sign * x < sign * xd[1]) {
            n2 = 1;
        } else if (sign * x > sign * xd[n2 - 1]) {
            n1 = n2 - 1;
        } else {
            if (guessIndex > 0 && sign * x > sign * xd[guessIndex - 1]) {
                n1 = guessIndex - 1;
            }
            if (guessIndex < n2 && sign * x < sign * xd[guessIndex + 1]) {
                n2 = guessIndex + 1;
            }
        }
        while (n2 - n1 > 1) {
            int n = (n1 + n2) / 2;
            if (sign * xd[n] > sign * x) {
                n2 = n;
            } else {
                n1 = n;
            }
        }
        guessIndex = n1;
        double step = xd[n2] - xd[n1];
        double a = (xd[n2] - x) / step;
        double b = (x - xd[n1]) / step;
        return a * yd[n1] + b * yd[n2] + (a * (a * a - 1) * coefficients[n1] + b * (b * b - 1) * coefficients[n2]) * step * step / 6;
    }
