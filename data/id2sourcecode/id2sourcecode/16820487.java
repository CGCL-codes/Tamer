    public static double[] arrayShiftLeft(double[] x) {
        double[] temp = new double[x.length - 1];
        for (int i = 0; i < x.length; i++) {
            temp[i] = x[i + 1];
        }
        x = temp;
        return x;
    }
