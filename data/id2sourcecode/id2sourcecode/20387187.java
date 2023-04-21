    public void testFirstInRange() {
        for (int i = 0; i < 1000; i++) {
            BitVector v = new BitVector(1000);
            int vSize = v.size();
            int a = random.nextInt(vSize + 1);
            int b = a + random.nextInt(vSize + 1 - a);
            BitVector w = v.rangeView(a, b);
            int c;
            int wSize = w.size();
            if (wSize == 0) {
                c = -1;
            } else {
                c = random.nextInt(wSize);
                w.setBit(c, true);
            }
            if (c >= 0) {
                assertEquals(c, w.firstOne());
                assertEquals(c, w.lastOne());
                assertEquals(c, w.nextOne(c));
                assertEquals(-1, w.previousOne(c));
                if (c > 0) assertEquals(c, w.nextOne(c - 1));
                if (c < wSize) assertEquals(c, w.previousOne(c + 1));
                assertEquals(c, w.nextOne(0));
                assertEquals(c, w.previousOne(wSize));
            } else {
                assertEquals(0, w.firstOne());
                assertEquals(-1, w.lastOne());
            }
            w.flip();
            if (c >= 0) {
                assertEquals(c, w.firstZero());
                assertEquals(c, w.lastZero());
                assertEquals(c, w.nextZero(c));
                assertEquals(-1, w.previousZero(c));
                if (c > 0) assertEquals(c, w.nextZero(c - 1));
                if (c < wSize) assertEquals(c, w.previousZero(c + 1));
                assertEquals(c, w.nextZero(0));
                assertEquals(c, w.previousZero(wSize));
            } else {
                assertEquals(0, w.firstZero());
                assertEquals(-1, w.lastZero());
            }
        }
    }
