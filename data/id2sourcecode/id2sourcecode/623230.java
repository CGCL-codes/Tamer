    private int huft_build(int[] b, int bindex, int n, int s, int[] d, int[] e, int[] t, int[] m, int[] hp, int[] hn, int[] v) {
        int a;
        int f;
        int g;
        int h;
        int i;
        int j;
        int k;
        int l;
        int mask;
        int p;
        int q;
        int w;
        int xp;
        int y;
        int z;
        p = 0;
        i = n;
        do {
            c[b[bindex + p]]++;
            p++;
            i--;
        } while (i != 0);
        if (c[0] == n) {
            t[0] = -1;
            m[0] = 0;
            return Z_OK;
        }
        l = m[0];
        for (j = 1; j <= BMAX; j++) if (c[j] != 0) break;
        k = j;
        if (l < j) {
            l = j;
        }
        for (i = BMAX; i != 0; i--) {
            if (c[i] != 0) break;
        }
        g = i;
        if (l > i) {
            l = i;
        }
        m[0] = l;
        for (y = 1 << j; j < i; j++, y <<= 1) {
            if ((y -= c[j]) < 0) {
                return Z_DATA_ERROR;
            }
        }
        if ((y -= c[i]) < 0) {
            return Z_DATA_ERROR;
        }
        c[i] += y;
        x[1] = j = 0;
        p = 1;
        xp = 2;
        while (--i != 0) {
            x[xp] = (j += c[p]);
            xp++;
            p++;
        }
        i = 0;
        p = 0;
        do {
            if ((j = b[bindex + p]) != 0) {
                v[x[j]++] = i;
            }
            p++;
        } while (++i < n);
        n = x[g];
        x[0] = i = 0;
        p = 0;
        h = -1;
        w = -l;
        u[0] = 0;
        q = 0;
        z = 0;
        for (; k <= g; k++) {
            a = c[k];
            while (a-- != 0) {
                while (k > w + l) {
                    h++;
                    w += l;
                    z = g - w;
                    z = (z > l) ? l : z;
                    if ((f = 1 << (j = k - w)) > a + 1) {
                        f -= a + 1;
                        xp = k;
                        if (j < z) {
                            while (++j < z) {
                                if ((f <<= 1) <= c[++xp]) break;
                                f -= c[xp];
                            }
                        }
                    }
                    z = 1 << j;
                    if (hn[0] + z > MANY) {
                        return Z_DATA_ERROR;
                    }
                    u[h] = q = hn[0];
                    hn[0] += z;
                    if (h != 0) {
                        x[h] = i;
                        r[0] = (byte) j;
                        r[1] = (byte) l;
                        j = i >>> (w - l);
                        r[2] = (int) (q - u[h - 1] - j);
                        System.arraycopy(r, 0, hp, (u[h - 1] + j) * 3, 3);
                    } else {
                        t[0] = q;
                    }
                }
                r[1] = (byte) (k - w);
                if (p >= n) {
                    r[0] = 128 + 64;
                } else if (v[p] < s) {
                    r[0] = (byte) (v[p] < 256 ? 0 : 32 + 64);
                    r[2] = v[p++];
                } else {
                    r[0] = (byte) (e[v[p] - s] + 16 + 64);
                    r[2] = d[v[p++] - s];
                }
                f = 1 << (k - w);
                for (j = i >>> w; j < z; j += f) {
                    System.arraycopy(r, 0, hp, (q + j) * 3, 3);
                }
                for (j = 1 << (k - 1); (i & j) != 0; j >>>= 1) {
                    i ^= j;
                }
                i ^= j;
                mask = (1 << w) - 1;
                while ((i & mask) != x[h]) {
                    h--;
                    w -= l;
                    mask = (1 << w) - 1;
                }
            }
        }
        return y != 0 && g != 1 ? Z_BUF_ERROR : Z_OK;
    }
