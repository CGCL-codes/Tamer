    public static double atan(double x) {
        double a, b;
        a = 1 / Math.sqrt(1 + (x * x));
        b = 1;
        for (int n = 1; n <= 15; n++) {
            a = (a + b) / 2;
            b = Math.sqrt(a * b);
        }
        return x / (Math.sqrt(1 + (x * x)) * a);
    }
