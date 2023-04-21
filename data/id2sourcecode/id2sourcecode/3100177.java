    public void test_connectLjava_io_PipedReader() throws Exception {
        char[] buf = new char[10];
        "HelloWorld".getChars(0, 10, buf, 0);
        PipedReader rd = new PipedReader();
        pw = new PipedWriter();
        pw.connect(rd);
        rdrThread = new Thread(reader = new PReader(rd), "connect");
        rdrThread.start();
        pw.write(buf);
        pw.close();
        rdrThread.join(500);
        assertEquals("Failed to write correct chars", "HelloWorld", new String(reader.buf));
    }
