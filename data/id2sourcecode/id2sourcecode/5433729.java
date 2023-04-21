    public void testInt() {
        IntArrayBitWriter writer = new IntArrayBitWriter(30000);
        IntArrayBitReader reader = new IntArrayBitReader(writer.getInts(), 30000);
        for (int i = -10000; i < 10000; i++) {
            checkInt(writer, reader, i);
        }
        checkInt(writer, reader, 1 - (1 << 30));
        checkInt(writer, reader, -(1 << 30));
        checkInt(writer, reader, 1 - (1 << 30));
        checkInt(writer, reader, (1 << 30));
        checkInt(writer, reader, 1 + (1 << 30));
        checkInt(writer, reader, -(1 << 30) - 1);
        Random r = new Random(0L);
        for (int i = 0; i < 1000000; i++) {
            checkInt(writer, reader, r.nextInt());
        }
    }
