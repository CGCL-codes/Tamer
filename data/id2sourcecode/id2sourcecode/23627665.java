    public void testReadWriteMixed() throws Exception {
        PipeBuffer pb = new PipeBuffer();
        ByteArrayOutputStream testInBytes = new ByteArrayOutputStream(10000);
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream(10000);
        writePacket(13, pb, testInBytes);
        assertEquals(0, pb.packetsAvailable());
        writeBytes(44, pb, testInBytes);
        assertEquals(1, pb.packetsAvailable());
        writePacket(1024, pb, testInBytes);
        assertEquals(3, pb.packetsAvailable());
        readBytes(100, pb, testOutBytes);
        readPacket(pb, testOutBytes);
        writeBytes(7053, pb, testInBytes);
        writePacket(42, pb, testInBytes);
        readBytes(2013, pb, testOutBytes);
        readBytes(2015, pb, testOutBytes);
        writePacket(7053, pb, testInBytes);
        while (pb.hasRemaining()) {
            readBytes((int) (pb.remaining() / 2), pb, testOutBytes);
            readPacket(pb, testOutBytes);
        }
        writeBytes(7053, pb, testInBytes);
        readPacket(pb, testOutBytes);
        writePacket(13, pb, testInBytes);
        writePacket(13, pb, testInBytes);
        while (pb.hasRemaining()) {
            readBytes((int) (pb.remaining() / 2), pb, testOutBytes);
            readPacket(pb, testOutBytes);
        }
        byte[] writeBytes = testInBytes.toByteArray();
        byte[] readBytes = testOutBytes.toByteArray();
        assertTrue(Arrays.equals(writeBytes, readBytes));
    }
