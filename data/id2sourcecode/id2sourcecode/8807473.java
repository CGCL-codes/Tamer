    @Test
    public void testLargeFile() throws Exception {
        ServerHandler hdl = new ServerHandler();
        Server server = new Server(hdl);
        server.start();
        File file = QAUtil.createTestfile_800k();
        IBlockingConnection connection = new BlockingConnection("localhost", server.getLocalPort());
        connection.write(file.getAbsolutePath() + "\r\n");
        int length = connection.readInt();
        File tempFile = QAUtil.createTempfile();
        RandomAccessFile raf = new RandomAccessFile(tempFile, "rw");
        FileChannel fc = raf.getChannel();
        connection.transferTo(fc, length);
        fc.close();
        raf.close();
        if (!QAUtil.isEquals(tempFile, file)) {
            System.out.println("files are not equals");
            Assert.fail("files are not equals");
        }
        file.delete();
        tempFile.delete();
        connection.close();
        server.close();
    }
