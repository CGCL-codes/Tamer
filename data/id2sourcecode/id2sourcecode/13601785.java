    public static double agmean(double x, double y) {
        for (int i = 0; i < 10; i++) {
            double a = (x + y) / 2;
            double g = Math.sqrt(x * y);
            x = a;
            y = g;
        }
        return x;
    }
