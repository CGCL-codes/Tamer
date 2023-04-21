    public void testWriteBit() {
        Random r = new Random();
        for (int i = 0; i < 100; i++) {
            BitWriter writer = newBitWriter(64);
            long bits = r.nextLong();
            for (int j = 63; j >= 0; j--) {
                writer.writeBit((int) (bits >> j));
            }
            writer.flush();
            BitReader reader = bitReaderFor(writer);
            long back = reader.readLong(64);
            assertEquals(bits, back);
        }
    }
