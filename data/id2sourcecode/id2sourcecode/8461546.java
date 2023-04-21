    private static void quickPartition(float[] x, int[] i, int[] m) {
        int p = m[0];
        int q = m[1];
        int n = q - p + 1;
        int k = (p + q) / 2;
        if (n > NSMALL_SORT) {
            int j = p;
            int l = q;
            if (n > NLARGE_SORT) {
                int s = n / 8;
                j = med3(x, i, j, j + s, j + 2 * s);
                k = med3(x, i, k - s, k, k + s);
                l = med3(x, i, l - 2 * s, l - s, l);
            }
            k = med3(x, i, j, k, l);
        }
        float y = x[i[k]];
        int a = p, b = p;
        int c = q, d = q;
        while (true) {
            while (b <= c && x[i[b]] <= y) {
                if (x[i[b]] == y) swap(i, a++, b);
                ++b;
            }
            while (c >= b && x[i[c]] >= y) {
                if (x[i[c]] == y) swap(i, c, d--);
                --c;
            }
            if (b > c) break;
            swap(i, b, c);
            ++b;
            --c;
        }
        int r = Math.min(a - p, b - a);
        int s = Math.min(d - c, q - d);
        int t = q + 1;
        swap(i, p, b - r, r);
        swap(i, b, t - s, s);
        m[0] = p + (b - a);
        m[1] = q - (d - c);
    }
