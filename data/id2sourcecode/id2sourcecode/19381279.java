    public void normalize() {
        final double sum = moments[0][0];
        for (int degree = 0; degree < moments.length; degree++) {
            final double pow = (degree + 2) / 2;
            final double factor = 1.0 / Math.pow(sum, pow);
            final double[] ithMoments = moments[degree];
            for (int i = 0; i < ithMoments.length; i++) {
                ithMoments[i] *= factor;
            }
        }
    }
