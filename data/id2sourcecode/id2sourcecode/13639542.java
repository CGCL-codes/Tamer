    @Test
    public void testNonBlockingTransferFrom() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        server.start();
        INonBlockingConnection clientCon = new NonBlockingConnection("localhost", server.getLocalPort());
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
        long transfered = fc.transferFrom(clientCon, 0, 9000000);
        fc.close();
        raf.close();
        Assert.assertEquals(txt.length(), transfered);
        Assert.assertTrue(QAUtil.isEquals(file, txt));
        file.delete();
        clientCon.close();
        server.close();
    }
