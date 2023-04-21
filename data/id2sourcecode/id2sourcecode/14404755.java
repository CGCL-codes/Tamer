    public static double computeRoot(final double left, final double right, final SingleParameterFunction function, final int iterations) {
        CDebug.checkParameterNotNull(function, "function");
        CDebug.checkParameterTrue(iterations > 0, "The number of iterations must be positive but is: " + iterations);
        double a = left < right ? left : right;
        double b = right > left ? right : left;
        checkIntervalForRoot(a, function.f(a), b, function.f(b));
        double c = (a + b) / 2;
        double fc = function.f(c);
        for (int i = 1; i < iterations && fc != 0.0; ++i) {
            if (function.f(a) * fc > 0.0) {
                a = c;
            } else {
                b = c;
            }
            c = (a + b) / 2;
            fc = function.f(c);
        }
        return c;
    }
