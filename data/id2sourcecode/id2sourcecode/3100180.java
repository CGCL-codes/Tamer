    public void test_write$CII() throws Exception {
        char[] buf = new char[10];
        "HelloWorld".getChars(0, 10, buf, 0);
        pw = new PipedWriter();
        rdrThread = new Thread(reader = new PReader(pw), "writeCII");
        rdrThread.start();
        pw.write(buf, 0, 10);
        pw.close();
        rdrThread.join(1000);
        assertEquals("Failed to write correct chars", "HelloWorld", new String(reader.buf));
    }
