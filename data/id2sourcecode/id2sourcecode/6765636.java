    private double transform(double val) {
        val = 2 * val - 1;
        val = Math.signum(val) * Math.pow(Math.abs(val), 1.0 / sharpness);
        val = Math.sin((Math.PI / 2) * val);
        val = (1 + val) / 2;
        return val;
    }
