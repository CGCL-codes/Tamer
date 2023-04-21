    private int[] merge(int[] source, int a, int b) {
        for (int k = a; k <= b; k++) tmp[k] = source[k];
        int m = (a + b) / 2;
        int i = a;
        int j = m + 1;
        for (int k = a; k <= b; k++) {
            if (tmp[i] < tmp[j]) {
                source[k] = tmp[i++];
                if (i > m) {
                    i = m;
                    tmp[i] = Integer.MAX_VALUE;
                }
            } else {
                source[k] = tmp[j++];
                if (j > b) {
                    j = b;
                    tmp[j] = Integer.MAX_VALUE;
                }
            }
        }
        return source;
    }
