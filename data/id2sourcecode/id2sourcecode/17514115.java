    @Test
    public void testTransferConnectionToFileChannel() throws Exception {
        long length = file.length();
        IServer server1 = new Server(new UploadHandler1());
        ConnectionUtils.start(server1);
        IServer server2 = new Server(new UploadHandler2());
        ConnectionUtils.start(server2);
        IBlockingConnection con1 = new BlockingConnection("localhost", server1.getLocalPort());
        IBlockingConnection con2 = new BlockingConnection("localhost", server2.getLocalPort());
        load(con1, length, 50);
        QAUtil.sleep(500);
        System.gc();
        QAUtil.sleep(1000);
        load(con2, length, 50);
        QAUtil.sleep(500);
        System.gc();
        QAUtil.sleep(1000);
        long elapsed1 = load(con1, length, 100);
        QAUtil.sleep(500);
        System.gc();
        QAUtil.sleep(1000);
        long elapsed2 = load(con2, length, 100);
        int p = (int) (elapsed2 * 100 / elapsed1);
        System.out.println("elapsed fc controlled " + elapsed1 + " millis, elapsed xSocket connection controlled " + elapsed2 + " millis (" + p + "%)");
        if (elapsed2 > (elapsed1 * 110)) {
            String msg = "connection.transferFrom(fc) should be faster than fc.transferTo(0, fc.size(), connection)";
            System.out.println(msg);
            Assert.fail(msg);
        }
        System.gc();
        con1.close();
        con2.close();
        server1.close();
        server2.close();
    }
