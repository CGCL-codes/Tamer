    public final void shuttlesort(int[] ai, int[] ai1, int i, int j) {
        if ((j - i) < 2) {
            return;
        }
        final int k = (i + j) / 2;
        shuttlesort(ai1, ai, i, k);
        shuttlesort(ai1, ai, k, j);
        int l = i;
        int i1 = k;
        if (((j - i) >= 4) && (compare(ai[k - 1], ai[k]) <= 0)) {
            for (int j1 = i; j1 < j; j1++) {
                ai1[j1] = ai[j1];
            }
            return;
        }
        for (int k1 = i; k1 < j; k1++) {
            if ((i1 >= j) || ((l < k) && (compare(ai[l], ai[i1]) <= 0))) {
                ai1[k1] = ai[l++];
            } else {
                ai1[k1] = ai[i1++];
            }
        }
    }
