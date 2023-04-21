    public void compact(int i) {
        for (int j = i; j < K - 1; j++) {
            r[j] = r[j + 1];
            w[j] = w[j + 1];
            wm[j] = wm[j + 1];
        }
        K--;
    }
