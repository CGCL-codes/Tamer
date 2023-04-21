    public void test() throws Exception {
        PipedConnection rightSide = new PipedConnection(new Object[0]);
        PipedConnection leftSide = new PipedConnection(new Object[] { rightSide });
        byte theByte[] = new byte[1];
        Reader reader = new Reader(rightSide, theByte);
        Writer writer = new Writer(leftSide, theByte, reader);
        reader.start();
        writer.start();
        Thread.sleep(2000);
        writer.term();
        writer.join();
        reader.join();
        assure("", writer._state && reader._state);
    }
