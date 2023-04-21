    private static int partition(int[] a, int l, int r) {
        int x = a[r];
        int lp = l - 1;
        for (int rp = l; rp < r; rp++) {
            if (a[rp] <= x) {
                lp++;
                int tmp = a[rp];
                a[rp] = a[lp];
                a[lp] = tmp;
            }
        }
        a[r] = a[lp + 1];
        a[lp + 1] = x;
        return lp;
    }
