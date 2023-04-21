    public static double median(double[] values) {
        double[] v = new double[values.length];
        double median = Double.NaN;
        if ((v == null) || (v.length == 0)) {
            throw new IllegalArgumentException("The data array either is null or does not contain any data.");
        } else if (v.length == 1) {
            median = v[0];
        } else {
            System.arraycopy(values, 0, v, 0, values.length);
            Arrays.sort(v);
            if (DoubleMath.isEven(v.length)) {
                int i = (int) Math.ceil(v.length / 2D);
                double n1 = v[i];
                double n0 = v[i - 1];
                median = (n0 + n1) / 2;
            } else {
                median = v[v.length / 2];
            }
        }
        return median;
    }
