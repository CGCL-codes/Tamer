    @Test
    public void testSmallFile() throws Exception {
        ServerHandler hdl = new ServerHandler();
        Server server = new Server(hdl);
        server.start();
        File file = QAUtil.createTestfile_4k();
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.write(file.getAbsolutePath() + "\r\n");
        int length = connection.readInt();
        File tempFile = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        FileChannel fc = raf.getChannel();
        connection.transferTo(fc, length);
        fc.close();
        raf.close();
        Assert.assertTrue(QAUtil.isEquals(tempFile, file));
        file.delete();
        tempFile.delete();
        connection.close();
        server.close();
    }
