    public static void smoothByCubic(double[] curve, int times) {
        int size = curve.length;
        double[] xn = new double[] { 0, 1, 3, 4 };
        double[] yn = new double[4];
        for (int t = 0; t < times; t++) {
            for (int x = 2; x < size - 2; x++) {
                yn[0] = curve[x - 2];
                yn[1] = curve[x - 1];
                yn[2] = curve[x + 1];
                yn[3] = curve[x + 2];
                curve[x] = Interpolation.cubic(xn, yn, 2);
            }
        }
    }
