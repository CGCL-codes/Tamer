    public void test_writeI() throws Exception {
        pw = new PipedWriter();
        rdrThread = new Thread(reader = new PReader(pw), "writeI");
        rdrThread.start();
        pw.write(1);
        pw.write(2);
        pw.write(3);
        pw.close();
        rdrThread.join(1000);
        assertTrue("Failed to write correct chars: " + (int) reader.buf[0] + " " + (int) reader.buf[1] + " " + (int) reader.buf[2], reader.buf[0] == 1 && reader.buf[1] == 2 && reader.buf[2] == 3);
    }
