    private void removeObservation(int index) {
        final int N = y.numRows - 1;
        final double d[] = y.data;
        for (int i = index; i < N; i++) {
            d[i] = d[i + 1];
        }
        y.numRows--;
    }
