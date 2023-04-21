    private static void insertSort2(int[] a) {
        int lo = 0;
        int up = a.length;
        int tempr = 0;
        int j = 0;
        int l, h = 0;
        for (int i = lo + 1; i < up; i++) {
            tempr = a[i];
            for (l = lo - 1, h = i; h - l > 1; ) {
                j = (h + l) / 2;
                if (tempr < a[j]) h = j; else l = j;
            }
            for (j = i; j > h; j--) a[j] = a[j - 1];
            a[h] = tempr;
        }
    }
