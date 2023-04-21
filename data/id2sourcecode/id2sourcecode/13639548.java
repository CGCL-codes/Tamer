    @Test
    public void testTransferTo() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        server.start();
        INonBlockingConnection clientCon = new NonBlockingConnection("localhost", server.getLocalPort());
        QAUtil.sleep(1000);
        INonBlockingConnection serverCon = srvHdl.getConection();
        File file = QAUtil.createTestfile_40k();
        RandomAccessFile raf = new RandomAccessFile(file, "r");
        FileChannel fc = raf.getChannel();
        fc.transferTo(0, fc.size(), clientCon);
        QAUtil.sleep(200);
        ByteBuffer[] buffer = serverCon.readByteBufferByLength(serverCon.available());
        Assert.assertTrue(QAUtil.isEquals(file, buffer));
        raf.close();
        file.delete();
        clientCon.close();
        server.close();
    }
