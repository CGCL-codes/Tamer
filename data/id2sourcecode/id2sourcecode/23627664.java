    public void testReadWritePackets() throws Exception {
        PipeBuffer pb = new PipeBuffer();
        ByteArrayOutputStream testInBytes = new ByteArrayOutputStream(10000);
        ByteArrayOutputStream testOutBytes = new ByteArrayOutputStream(10000);
        writePacket(13, pb, testInBytes);
        assertEquals(0, pb.packetsAvailable());
        writePacket(44, pb, testInBytes);
        assertEquals(1, pb.packetsAvailable());
        writePacket(1024, pb, testInBytes);
        assertEquals(3, pb.packetsAvailable());
        readPacket(pb, testOutBytes);
        writePacket(7053, pb, testInBytes);
        writePacket(42, pb, testInBytes);
        readPacket(pb, testOutBytes);
        writePacket(7053, pb, testInBytes);
        while (pb.hasRemaining()) {
            readPacket(pb, testOutBytes);
        }
        writePacket(7053, pb, testInBytes);
        readPacket(pb, testOutBytes);
        writePacket(13, pb, testInBytes);
        writePacket(13, pb, testInBytes);
        while (pb.hasRemaining()) {
            readPacket(pb, testOutBytes);
        }
        byte[] writeBytes = testInBytes.toByteArray();
        byte[] readBytes = testOutBytes.toByteArray();
        assertTrue(Arrays.equals(writeBytes, readBytes));
    }
