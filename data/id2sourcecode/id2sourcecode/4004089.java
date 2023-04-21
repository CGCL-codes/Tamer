    public void setParameters(double a, double b) {
        if (a < b) {
            minValue = a;
            maxValue = b;
        } else {
            minValue = a;
            maxValue = a + 1;
        }
        alpha = (12.0) / ((maxValue - minValue) * (maxValue - minValue) * (maxValue - minValue));
        beta = (maxValue + minValue) / 2;
        double step = 0.01 * (maxValue - minValue);
        super.setParameters(minValue, maxValue, step, CONTINUOUS);
    }
