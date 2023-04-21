    public static double computeRoot(final double left, final double right, final SingleParameterFunction function, final double accuracy) {
        CDebug.checkParameterNotNull(function, "function");
        CDebug.checkParameterTrue(accuracy > 0, "Accuracy must be positive but is: " + accuracy);
        double a = left < right ? left : right;
        double b = right > left ? right : left;
        checkIntervalForRoot(a, function.f(a), b, function.f(b));
        double c = (a + b) / 2;
        double fc = function.f(c);
        final int iterations = 1 + (int) ((Math.log(b - a) - Math.log(accuracy)) / Math.log(2));
        for (int i = 1; i <= iterations && Math.abs(fc) > accuracy; ++i) {
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
