    public void test_ConstructorLjava_io_PipedReader() throws Exception {
        char[] buf = new char[10];
        "HelloWorld".getChars(0, 10, buf, 0);
        PipedReader rd = new PipedReader();
        pw = new PipedWriter(rd);
        rdrThread = new Thread(reader = new PReader(rd), "Constructor(Reader)");
        rdrThread.start();
        pw.write(buf);
        pw.close();
        rdrThread.join(500);
        assertEquals("Failed to construct writer", "HelloWorld", new String(reader.buf));
    }
