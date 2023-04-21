    private static double[] createDerivation(double[] values) {
        double[] derivedValues = new double[values.length - 1];
        for (int i = 0; i < derivedValues.length; i++) {
            derivedValues[i] = values[i + 1] - values[i];
        }
        return derivedValues;
    }
