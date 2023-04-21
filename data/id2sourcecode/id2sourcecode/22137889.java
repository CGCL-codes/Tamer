    public void testRead() throws Exception {
        TestEncodingInputStream istream = new TestEncodingInputStream();
        ByteArrayOutputStream testInBytes = new ByteArrayOutputStream(10000);
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream(10000);
        writeBytes(13, istream, testInBytes);
        readBytes(13, istream, testOutBytes, false);
        writeBytes(42, istream, testInBytes);
        readBytes(42, istream, testOutBytes, false);
        writeBytes(1024, istream, testInBytes);
        readBytes(1024, istream, testOutBytes, false);
        writeBytes(7053, istream, testInBytes);
        readBytes(7053, istream, testOutBytes, false);
        writeBytes(1024, istream, testInBytes);
        readBytes(1024, istream, testOutBytes, false);
        writeBytes(42, istream, testInBytes);
        readBytes(42, istream, testOutBytes, false);
        writeBytes(7053, istream, testInBytes);
        readBytes(7053, istream, testOutBytes, false);
        writeBytes(7053, istream, testInBytes);
        readBytes(7053, istream, testOutBytes, false);
        writeBytes(13, istream, testInBytes);
        readBytes(13, istream, testOutBytes, false);
        writeBytes(13, istream, testInBytes);
        readBytes(13, istream, testOutBytes, false);
        readBytes(109, istream, testOutBytes, true);
        byte[] writeBytes = testInBytes.toByteArray();
        byte[] readBytes = testOutBytes.toByteArray();
        assertTrue(Arrays.equals(writeBytes, readBytes));
    }
