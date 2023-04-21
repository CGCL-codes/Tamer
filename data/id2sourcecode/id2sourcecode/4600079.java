    @Test
    public void testTransferFrom() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        ConnectionUtils.start(server);
        INonBlockingConnection clientCon = new NonBlockingConnection("localhost", server.getLocalPort());
        QAUtil.sleep(200);
        INonBlockingConnection serverCon = srvHdl.getConection();
        clientCon.setFlushmode(FlushMode.ASYNC);
        File file = QAUtil.createTestfile_40k();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        clientCon.transferFrom(fc);
        QAUtil.sleep(500);
        ByteBuffer[] bufs = serverCon.readByteBufferByLength(serverCon.available());
        Assert.assertTrue(QAUtil.isEquals(file, bufs));
        raf.close();
        file.delete();
        clientCon.close();
        server.close();
    }
