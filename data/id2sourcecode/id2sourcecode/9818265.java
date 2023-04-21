    @Test
    public void testFile() throws Exception {
        File file = File.createTempFile("ere", "rsasd");
        file.deleteOnExit();
        FileChannel fc = new RandomAccessFile(file, "rw").getChannel();
        IServer server = new Server(new Handler(fc));
        StreamUtils.start(server);
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.setAutoflush(false);
        byte[] request = QAUtil.generateByteArray(60000);
        connection.write(request);
        connection.flush();
        connection.write(DELIMITER);
        connection.flush();
        connection.close();
        QAUtil.sleep(500);
        Assert.assertTrue(fc.size() == 60000);
        fc.close();
        file.delete();
        server.close();
    }
