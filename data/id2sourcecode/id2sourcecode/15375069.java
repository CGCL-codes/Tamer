    private void testRelativeReaderAndWriter(int length, IoRelativeReader reader, IoRelativeWriter writer) {
        for (int i = 0; i < length; i++) {
            byte b = (byte) (i % 67);
            writer.put(b);
            assertEquals(b, reader.get());
        }
    }
