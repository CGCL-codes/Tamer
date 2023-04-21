    public void testReadWrite() throws Exception {
        PipeBuffer pb = new PipeBuffer();
        ByteArrayOutputStream testInBytes = new ByteArrayOutputStream(10000);
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream(10000);
        writeBytes(13, pb, testInBytes);
        writeBytes(45, pb, testInBytes);
        writeBytes(1024, pb, testInBytes);
        readBytes(100, pb, testOutBytes);
        writeBytes(7053, pb, testInBytes);
        writeBytes(42, pb, testInBytes);
        readBytes(7053, pb, testOutBytes);
        writeBytes(7053, pb, testInBytes);
        readBytes((int) pb.remaining(), pb, testOutBytes);
        writeBytes(7053, pb, testInBytes);
        readBytes(400, pb, testOutBytes);
        writeBytes(13, pb, testInBytes);
        writeBytes(13, pb, testInBytes);
        readBytes((int) pb.remaining(), pb, testOutBytes);
        byte[] writeBytes = testInBytes.toByteArray();
        byte[] readBytes = testOutBytes.toByteArray();
        assertTrue(Arrays.equals(writeBytes, readBytes));
    }
