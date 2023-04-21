    private static int backingArray132(IBackingArray a, long ia, int n1, int n2, int n3) {
        IBackingArray b, c, d;
        int i, j, k, im, mn, ncount;
        long ij, ji, n1l;
        int move_size = (n3 + n2) / 2;
        byte[] move = new byte[move_size];
        if (move == null) throw new OutOfMemoryError("Could not allocate work array");
        if (n2 < 0 || n3 < 0) throw new IllegalArgumentException("n3,n2 < 0");
        if (n2 < 2 || n3 < 2) return 0;
        if (move_size < 1) throw new IllegalArgumentException("move_size < 1");
        n1l = n1;
        b = BackingArray.factory(a.getClassType(), n1, BackingArray.Type.JAVA_ARRAY);
        if (b == null) throw new OutOfMemoryError("Could not allocate work array");
        if (n2 == n3) {
            for (i = 0; i < n3; ++i) for (j = i + 1; j < n3; ++j) {
                ij = ia + n1 * (i + j * n3);
                ji = ia + n1 * (j + i * n3);
                BackingArray.arraycopy(a, ij, b, 0l, n1l);
                BackingArray.arraycopy(a, ji, a, ij, n1l);
                BackingArray.arraycopy(b, 0l, a, ji, n1l);
            }
            return 0;
        }
        c = BackingArray.factory(a.getClassType(), n1, BackingArray.Type.JAVA_ARRAY);
        if (c == null) {
            b = null;
            throw new OutOfMemoryError("Could not allocate work array");
        }
        ncount = 2;
        k = (mn = n2 * n3) - 1;
        for (i = 0; i < move_size; ++i) move[i] = 0;
        if (n2 >= 3 && n3 >= 3) ncount += TOMS_gcd(n2 - 1, n3 - 1) - 1;
        i = 1;
        im = n2;
        while (true) {
            int i1, i2, i1c, i2c;
            int kmi;
            i1 = i;
            kmi = k - i;
            ij = ia + n1 * i1;
            BackingArray.arraycopy(a, ij, b, 0l, n1l);
            i1c = kmi;
            ij = ia + n1 * i1c;
            BackingArray.arraycopy(a, ij, c, 0l, n1l);
            d = BackingArray.factory(a.getClassType(), n1, BackingArray.Type.JAVA_ARRAY);
            while (true) {
                i2 = n2 * i1 - k * (i1 / n3);
                i2c = k - i2;
                if (i1 < move_size) move[i1] = 1;
                if (i1c < move_size) move[i1c] = 1;
                ncount += 2;
                if (i2 == i) break;
                if (i2 == kmi) {
                    BackingArray.arraycopy(b, 0, d, 0, n1);
                    BackingArray.arraycopy(c, 0, b, 0, n1);
                    BackingArray.arraycopy(d, 0, c, 0, n1);
                    break;
                }
                ij = ia + n1 * i2;
                ji = ia + n1 * i1;
                BackingArray.arraycopy(a, ij, a, ji, n1l);
                ij = ia + n1 * i2c;
                ji = ia + n1 * i1c;
                BackingArray.arraycopy(a, ij, a, ji, n1l);
                i1 = i2;
                i1c = i2c;
            }
            ij = ia + n1 * i1;
            BackingArray.arraycopy(b, 0l, a, ij, n1l);
            ij = ia + n1 * i1c;
            BackingArray.arraycopy(c, 0l, a, ij, n1l);
            if (ncount >= mn) break;
            while (true) {
                int max;
                max = k - i;
                ++i;
                if (i > max) return i;
                im += n2;
                if (im > k) im -= k;
                i2 = im;
                if (i == i2) continue;
                if (i >= move_size) {
                    while (i2 > i && i2 < max) {
                        i1 = i2;
                        i2 = n2 * i1 - k * (i1 / n3);
                    }
                    if (i2 == i) break;
                } else if (move[i] == 0) break;
            }
        }
        return 0;
    }
