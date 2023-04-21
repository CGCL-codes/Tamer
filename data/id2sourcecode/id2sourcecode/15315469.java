    public void testUDP() throws Exception {
        DatagramSocket client = new DatagramSocket();
        client.connect(new InetSocketAddress("127.0.0.1", port));
        client.setSoTimeout(500);
        byte[] writeBuf = new byte[16];
        byte[] readBuf = new byte[writeBuf.length];
        DatagramPacket wp = new DatagramPacket(writeBuf, writeBuf.length);
        DatagramPacket rp = new DatagramPacket(readBuf, readBuf.length);
        for (int i = 0; i < 10; i++) {
            fillWriteBuffer(writeBuf, i);
            client.send(wp);
            client.receive(rp);
            assertEquals(writeBuf.length, rp.getLength());
            assertTrue(Arrays.equals(writeBuf, readBuf));
        }
        try {
            client.receive(rp);
            fail("Unexpected incoming data.");
        } catch (SocketTimeoutException e) {
        }
        client.close();
    }
