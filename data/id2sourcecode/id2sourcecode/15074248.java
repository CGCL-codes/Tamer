    public void testClientRead() throws InterruptedException, IOException {
        String sendString = "info, 1000, sine, 5e-1, 2";
        serverThread.writeString(sendString);
        Thread.sleep(100);
        assertEquals(sendString, clientConn.read());
    }
