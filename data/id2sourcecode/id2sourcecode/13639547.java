    @Test
    public void testBlockingTransferFromClientClosed() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        server.start();
        IBlockingConnection clientCon = new BlockingConnection("localhost", server.getLocalPort());
        QAUtil.sleep(1000);
        INonBlockingConnection serverCon = srvHdl.getConection();
        serverCon.setAutoflush(false);
        File file = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fc = raf.getChannel();
        String txt = "Hello my client\r\n";
        serverCon.write(txt);
        serverCon.flush();
        QAUtil.sleep(200);
        clientCon.close();
        try {
            fc.transferFrom(clientCon, 0, 9000000);
            Assert.fail("ClosedChannelException expected");
        } catch (ClosedChannelException expected) {
        }
        file.delete();
        server.close();
        clientCon.close();
    }
