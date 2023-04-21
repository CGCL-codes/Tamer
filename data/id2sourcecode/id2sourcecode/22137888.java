    public void testReadPackets() throws Exception {
        TestEncodingInputStream istream = new TestEncodingInputStream();
        byte[] writePacket = new byte[1024];
        istream._toWrite = writePacket;
        istream._writePacket = true;
        byte[] readPacket = istream.readPacket();
        assertTrue(readPacket == writePacket);
    }
