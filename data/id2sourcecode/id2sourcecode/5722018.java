    public void testOverride() throws Exception {
        serverAcceptsWait();
        Socket socket = new LocalSocket(new LocalSocketAddress(socketName, socketAbstractNamespace));
        OutputStream out = socket.getOutputStream();
        out.write(1);
        assertNull(socket.getChannel());
        try {
            socket.getInetAddress();
            fail("getInetAddress");
        } catch (IllegalArgumentException e) {
        }
        try {
            socket.getLocalAddress();
            fail("getLocalAddress");
        } catch (IllegalArgumentException e) {
        }
        try {
            socket.getPort();
            fail("getPort");
        } catch (IllegalArgumentException e) {
        }
        try {
            socket.getLocalPort();
            fail("getLocalPort");
        } catch (IllegalArgumentException e) {
        }
        InputStream in = socket.getInputStream();
        assertEquals(1, in.read());
        assertEquals(-1, in.read());
        in.close();
        out.close();
        socket.close();
        assertServerErrors();
    }
