    public void testSetReadObserver() throws Exception {
        byte[] data = new byte[127];
        for (byte i = 0; i < data.length; i++) data[i] = i;
        Listener listener = new Listener(PORT);
        NIOSocket socket = new NIOSocket("127.0.0.1", PORT);
        Stream stream = listener.getStream();
        stream.write(data);
        InputStream in = socket.getInputStream();
        byte[] readIn = new byte[57];
        in.read(readIn);
        for (int i = 0; i < readIn.length; i++) assertEquals(i, readIn[i]);
        NIOTestUtils.waitForNIO();
        NIOInputStream nioInput = (NIOInputStream) PrivilegedAccessor.getValue(socket, "reader");
        ByteBuffer buffered = (ByteBuffer) PrivilegedAccessor.getValue(nioInput, "buffer");
        assertEquals(buffered.toString(), data.length - readIn.length, buffered.position());
        ReadTester reader = new ReadTester();
        socket.setReadObserver(reader);
        NIOTestUtils.waitForNIO();
        ByteBuffer remaining = reader.getRead();
        assertEquals(remaining.toString(), data.length - readIn.length, remaining.remaining());
        for (int i = readIn.length; i < data.length; i++) assertEquals(i, remaining.get());
        assertInstanceof(SocketInterestReadAdapter.class, reader.getReadChannel());
        assertSame(socket.getChannel(), ((SocketInterestReadAdapter) reader.getReadChannel()).getChannel());
        socket.close();
        stream.socket.close();
    }
