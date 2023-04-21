    private double[] makeKernel(double radius) {
        radius += 1;
        int size = (int) radius * 2 + 1;
        double[] kernel = new double[size];
        double v = radius - (int) radius;
        for (int i = 0; i < size; i++) kernel[i] = (float) Math.exp(-0.5 * (sqr((i - (radius - v)) / ((radius) * 2))) / sqr(0.2));
        double[] kernel2 = new double[size - 2];
        for (int i = 0; i < size - 2; i++) kernel2[i] = kernel[i + 1];
        if (kernel2.length == 1) kernel2[0] = 1f;
        return kernel2;
    }
