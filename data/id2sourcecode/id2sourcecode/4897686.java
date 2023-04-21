    @Test
    public void testWriteFromStream() throws Exception {
        System.setProperty("org.xsocket.connection.suppressReuseBufferWarning", "true");
        IServer server = new Server(new ServerEchoHandler());
        server.start();
        INonBlockingConnection con = new NonBlockingConnection("localhost", server.getLocalPort());
        con.setFlushmode(FlushMode.ASYNC);
        File file = QAUtil.createTestfile_40k();
        Writer writer = new Writer(file.getAbsolutePath(), con);
        writer.write();
        do {
            QAUtil.sleep(300);
        } while (!writer.isComplete());
        File tempFile = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        FileChannel channel = raf.getChannel();
        con.transferTo(channel, (int) file.length());
        channel.close();
        raf.close();
        Assert.assertTrue(QAUtil.isEquals(file, tempFile));
        file.delete();
        tempFile.delete();
        con.close();
        server.close();
    }
