    @Test
    public void testBlockingTransferFromServerClosed() throws Exception {
        ServerHandler srvHdl = new ServerHandler();
        IServer server = new Server(srvHdl);
        server.start();
        IBlockingConnection clientCon = new BlockingConnection("localhost", server.getLocalPort());
        QAUtil.sleep(1000);
        final INonBlockingConnection serverCon = srvHdl.getConection();
        serverCon.setAutoflush(false);
        File file = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fc = raf.getChannel();
        String txt = "Hello my client\r\n";
        serverCon.write(txt);
        serverCon.flush();
        Thread t = new Thread() {

            @Override
            public void run() {
                QAUtil.sleep(300);
                try {
                    serverCon.close();
                } catch (IOException ioe) {
                    ioe.toString();
                }
                ;
            }
        };
        t.start();
        long transfered = fc.transferFrom(clientCon, 0, 9000000);
        fc.close();
        raf.close();
        Assert.assertEquals(txt.length(), transfered);
        Assert.assertTrue(QAUtil.isEquals(file, txt));
        Assert.assertFalse(clientCon.isOpen());
        file.delete();
        server.close();
    }
