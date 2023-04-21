    private void testPass(int size, long seed) {
        BitWriter writer = newBitWriter(size * 32);
        ArrayList<Point> list = new ArrayList<Point>(size);
        Random r = new Random(seed);
        for (int i = 0; i < size; i++) {
            int x = r.nextInt(33);
            int y = r.nextInt() & ((1 << x) - 1);
            writer.write(y, x);
            list.add(new Point(x, y));
        }
        long pos = writer.getPosition();
        writer.flush();
        assertEquals(0, writer.padToBoundary(getBoundary()));
        BitReader reader = bitReaderFor(writer);
        for (int i = 0; i < size; i++) {
            Point pt = list.get(i);
            int v = reader.read(pt.x);
            if (pt.y != v) throw new RuntimeException("Failed at " + i + ": " + v + " is not " + pt.y);
        }
        if (reader.getPosition() != pos) throw new RuntimeException();
    }
