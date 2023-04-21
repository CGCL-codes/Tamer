    static void unpadArray(double[] source, double[] target) {
        for (int i = 0; i < target.length; i++) {
            target[i] = source[i + 1];
        }
    }
