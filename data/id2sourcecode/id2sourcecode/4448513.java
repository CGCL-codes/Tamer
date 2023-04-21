    private static double round(final double a, final double b) {
        final double v = (a + b) / 2;
        final double d = v * 100000;
        final int i = (int) d;
        final double r = d - i >= 0.5 ? i + 1 : i;
        return r / 100000;
    }
