    @Test
    public void testCompositeByteArray() throws Exception {
        CompositeByteArray ba = new CompositeByteArray();
        for (int i = 0; i < 1000; i += 100) {
            ba.addLast(getByteArrayFactory().create(100));
        }
        resetOperations();
        testAbsoluteReaderAndWriter(0, 1000, ba, ba);
        testAbsoluteReaderAndWriter(0, 1000, ba, ba);
        assertOperationCountEquals(0);
        Cursor readCursor = ba.cursor();
        Cursor writeCursor = ba.cursor();
        testRelativeReaderAndWriter(1000, readCursor, writeCursor);
        assertOperationCountEquals(0);
    }
