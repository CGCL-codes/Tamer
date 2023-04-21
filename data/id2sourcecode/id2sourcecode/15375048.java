    @Test
    public void testBufferByteArray() throws Exception {
        ByteArray ba = getByteArrayFactory().create(1000);
        testAbsoluteReaderAndWriter(0, 1000, ba, ba);
        testAbsoluteReaderAndWriter(0, 1000, ba, ba);
        Cursor readCursor = ba.cursor();
        Cursor writeCursor = ba.cursor();
        testRelativeReaderAndWriter(1000, readCursor, writeCursor);
    }
