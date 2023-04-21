    private void testRuns(int size, long seed) {
        int maxrunlength = 8192;
        int asize = size * maxrunlength * 2;
        BitWriter writer = newBitWriter((asize + 31) / 32 * 32);
        ArrayList<Point> list = new ArrayList<Point>(size);
        Random r = new Random(1);
        for (int i = 0; i < size; i++) {
            long pos = writer.getPosition();
            int x = r.nextInt(maxrunlength);
            int y = r.nextInt(maxrunlength);
            writer.writeBooleans(false, x);
            assertEquals(pos + x, writer.getPosition());
            writer.writeBooleans(true, y);
            assertEquals(pos + x + y, writer.getPosition());
            list.add(new Point(x, y));
        }
        long pos = writer.getPosition();
        writer.flush();
        assertEquals(0, writer.padToBoundary(getBoundary()));
        BitReader reader = bitReaderFor(writer);
        for (int i = 0; i < size; i++) {
            Point pt = list.get(i);
            for (int x = 0; x < pt.x; x++) {
                if (reader.readBit() != 0) throw new RuntimeException("Failed at " + i + ": expected 0");
            }
            for (int y = 0; y < pt.y; y++) {
                int v = reader.readBit();
                if (v != 1) throw new RuntimeException("Failed at " + i + ": expected 1, got " + v);
            }
        }
        assertEquals(pos, reader.getPosition());
    }
