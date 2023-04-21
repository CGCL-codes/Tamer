    private void testTCP0(Socket client) throws Exception {
        client.setSoTimeout(3000);
        byte[] writeBuf = new byte[16];
        for (int i = 0; i < 10; i++) {
            fillWriteBuffer(writeBuf, i);
            client.getOutputStream().write(writeBuf);
        }
        byte[] readBuf = new byte[writeBuf.length];
        for (int i = 0; i < 10; i++) {
            fillWriteBuffer(writeBuf, i);
            int readBytes = 0;
            while (readBytes < readBuf.length) {
                int nBytes = client.getInputStream().read(readBuf, readBytes, readBuf.length - readBytes);
                if (nBytes < 0) {
                    fail("Unexpected disconnection.");
                }
                readBytes += nBytes;
            }
            assertTrue(Arrays.equals(writeBuf, readBuf));
        }
        client.setSoTimeout(500);
        try {
            client.getInputStream().read();
            fail("Unexpected incoming data.");
        } catch (SocketTimeoutException e) {
        }
        client.getInputStream().close();
        client.close();
    }
