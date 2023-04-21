    private static void packNum(long n, int e, ByteBuffer buf) {
        boolean minus = n < 0;
        buf.put(minus ? Tag.MINUS : Tag.PLUS);
        if (n == 0) return;
        if (n < 0) n = -n;
        for (; (e % 4) != 0; --e) n *= 10;
        while (n % 10000 == 0) {
            n /= 10000;
            e += 4;
        }
        e = e / 4 + packshorts(n);
        buf.put(scale(e, minus));
        packLongPart(buf, n, minus);
    }
