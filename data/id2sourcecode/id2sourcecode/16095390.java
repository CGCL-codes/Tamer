    @Test
    public void fileStreamingHandlerTest() throws Exception {
        IServer server = new Server(new FileStreamingHandler());
        server.start();
        File file = QAUtil.createTestfile_400k();
        IBlockingConnection con = new BlockingConnection("localhost", server.getLocalPort());
        con.setAutoflush(false);
        for (int i = 0; i < 5; i++) {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            int length = (int) fc.size();
            con.write(length);
            con.transferFrom(fc);
            con.flush();
            fc.close();
            fis.close();
            String fname = con.readStringByDelimiter("\r\n");
            QAUtil.isEquals(file, new File(fname));
            new File(fname).delete();
        }
        file.delete();
        con.close();
        server.close();
    }
