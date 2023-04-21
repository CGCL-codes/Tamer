    public void testSkip() throws Exception {
        TestEncodingInputStream istream = new TestEncodingInputStream();
        ByteArrayOutputStream testInBytes = new ByteArrayOutputStream(10000);
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream(10000);
        writeBytes(100, istream, testInBytes);
        readBytes(49, istream, testOutBytes, false);
        writeBytes(100, istream, testInBytes);
        assertEquals(100, istream.skip(100));
        readBytes(10, istream, testOutBytes, true);
        byte[] writeBytes = testInBytes.toByteArray();
        byte[] readBytes = testOutBytes.toByteArray();
        assertEquals(writeBytes.length, readBytes.length + 100);
        byte[] writeBytes2 = new byte[readBytes.length];
        System.arraycopy(writeBytes, 0, writeBytes2, 0, 49);
        System.arraycopy(writeBytes, 149, writeBytes2, 49, 51);
        assertTrue(Arrays.equals(writeBytes2, readBytes));
    }
