    @Test
    public void testReuseByteBuffer() throws Exception {
        IServer server = new Server(new ServeeHandler3());
        server.start();
        File file = QAUtil.createTestfile_400k();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        ReadableByteChannel fc = raf.getChannel();
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        ByteBuffer copyBuffer = ByteBuffer.allocate(4096);
        int read = 0;
        while (read >= 0) {
            read = fc.read(copyBuffer);
            copyBuffer.flip();
            if (read > 0) {
                connection.write(copyBuffer);
                if (copyBuffer.hasRemaining()) {
                    copyBuffer.compact();
                } else {
                    copyBuffer.clear();
                }
            }
        }
        ByteBuffer[] buffer = connection.readByteBufferByLength((int) file.length());
        Assert.assertTrue(QAUtil.isEquals(file, buffer));
        raf.close();
        file.delete();
        connection.close();
        server.close();
    }
