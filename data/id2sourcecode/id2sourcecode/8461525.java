    private static void quickPartition(int[] x, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, j, j + s, j + 2 * s);
                k = med3(x, k - s, k, k + s);
                l = med3(x, l - 2 * s, l - s, l);
            }
            k = med3(x, j, k, l);
        }
        int y = x[k];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[b] <= y) {
                if (x[b] == y) swap(x, a++, b);
                ++b;
            }
            while (c >= b && x[c] >= y) {
                if (x[c] == y) swap(x, c, d--);
                --c;
            }
            if (b > c) break;
            swap(x, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(x, p, b - r, r);
        swap(x, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }
