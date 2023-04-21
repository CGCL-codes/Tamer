    private static void flipDXT5Block(byte[] block, int h) {
        if (h == 1) return;
        byte c0 = block[0];
        byte c1 = block[1];
        bb.clear();
        bb.put(block, 2, 6).flip();
        bb.clear();
        long l = bb.getLong();
        long n = l;
        if (h == 2) {
            n = writeCode5(n, 0, 0, readCode5(l, 0, 1));
            n = writeCode5(n, 1, 0, readCode5(l, 1, 1));
            n = writeCode5(n, 2, 0, readCode5(l, 2, 1));
            n = writeCode5(n, 3, 0, readCode5(l, 3, 1));
            n = writeCode5(n, 0, 1, readCode5(l, 0, 0));
            n = writeCode5(n, 1, 1, readCode5(l, 1, 0));
            n = writeCode5(n, 2, 1, readCode5(l, 2, 0));
            n = writeCode5(n, 3, 1, readCode5(l, 3, 0));
        } else {
            n = writeCode5(n, 0, 0, readCode5(l, 0, 3));
            n = writeCode5(n, 1, 0, readCode5(l, 1, 3));
            n = writeCode5(n, 2, 0, readCode5(l, 2, 3));
            n = writeCode5(n, 3, 0, readCode5(l, 3, 3));
            n = writeCode5(n, 0, 1, readCode5(l, 0, 2));
            n = writeCode5(n, 1, 1, readCode5(l, 1, 2));
            n = writeCode5(n, 2, 1, readCode5(l, 2, 2));
            n = writeCode5(n, 3, 1, readCode5(l, 3, 2));
            n = writeCode5(n, 0, 2, readCode5(l, 0, 1));
            n = writeCode5(n, 1, 2, readCode5(l, 1, 1));
            n = writeCode5(n, 2, 2, readCode5(l, 2, 1));
            n = writeCode5(n, 3, 2, readCode5(l, 3, 1));
            n = writeCode5(n, 0, 3, readCode5(l, 0, 0));
            n = writeCode5(n, 1, 3, readCode5(l, 1, 0));
            n = writeCode5(n, 2, 3, readCode5(l, 2, 0));
            n = writeCode5(n, 3, 3, readCode5(l, 3, 0));
        }
        bb.clear();
        bb.putLong(n);
        bb.clear();
        bb.get(block, 2, 6).flip();
        assert c0 == block[0] && c1 == block[1];
    }
