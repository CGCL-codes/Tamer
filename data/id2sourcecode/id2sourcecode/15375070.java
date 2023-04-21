    private void testAbsoluteReaderAndWriter(int start, int length, IoAbsoluteReader reader, IoAbsoluteWriter writer) {
        for (int i = start; i < length; i++) {
            byte b = (byte) (i % 67);
            writer.put(i, b);
            assertEquals(b, reader.get(i));
        }
    }
