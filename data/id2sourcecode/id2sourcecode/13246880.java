    @Test
    public void testFile() throws Exception {
        System.out.println("test file");
        File file = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(file, "rw");
        FileChannel fc = raf.getChannel();
        IServer server = new Server(new Handler(fc));
        server.start();
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.setAutoflush(false);
        byte[] request = QAUtil.generateByteArray(60000);
        connection.write(request);
        connection.flush();
        connection.write(DELIMITER);
        connection.flush();
        connection.close();
        QAUtil.sleep(1000);
        Assert.assertTrue(fc.size() == 60000);
        file.delete();
        fc.close();
        raf.close();
        file.delete();
        server.close();
    }
