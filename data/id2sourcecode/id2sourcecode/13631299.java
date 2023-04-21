    @Test
    public void testReadAndWrite() {
        final byte[] writeArray = new byte[TEST_FILE_SIZE];
        for (int i = 0; i < TEST_FILE_SIZE; ++i) writeArray[i] = (byte) (Math.random() * 256);
        final byte[] readArray = new byte[TEST_FILE_SIZE];
        try {
            module.write(writeArray, 0, TEST_FILE_SIZE, 0);
            module.read(readArray, 0, TEST_FILE_SIZE, 0);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
        for (int i = 0; i < TEST_FILE_SIZE; ++i) if (writeArray[i] != readArray[i]) fail("values do not match");
    }
