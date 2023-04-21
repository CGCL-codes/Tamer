    private static int[] createWeights(int length, int base) {
        int[] weights = new int[length];
        weights[length - 1] = 1;
        for (int i = length - 2; i >= 0; i--) weights[i] = weights[i + 1] * base;
        return weights;
    }
