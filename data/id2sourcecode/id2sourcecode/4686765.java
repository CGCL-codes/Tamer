    public void test_socketChannel_read_write() throws Exception {
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.socket().bind(new InetSocketAddress(InetAddress.getLocalHost(), 49999));
        SocketChannel sc = SocketChannel.open();
        sc.connect(new InetSocketAddress(InetAddress.getLocalHost(), 49999));
        SocketChannel sock = ssc.accept();
        ByteBuffer[] buf = { ByteBuffer.allocate(10), null };
        try {
            sc.write(buf, 0, 2);
            fail("should throw NPE");
        } catch (NullPointerException e) {
        }
        ssc.close();
        sc.close();
        ByteBuffer target = ByteBuffer.allocate(10);
        assertEquals(-1, sock.read(target));
    }
