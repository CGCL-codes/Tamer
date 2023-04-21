    public static double[] shift(double[] data) {
        for (int i = 0; i < data.length - 1; i++) {
            data[i] = data[i + 1];
        }
        return data;
    }
