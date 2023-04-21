    public void testFlapHeader() {
        try {
            new FlapHeader(ByteBlock.wrap(new byte[0]));
            fail("Should not create flap header with empty data");
        } catch (IllegalArgumentException e) {
        }
        try {
            new FlapHeader(ByteBlock.wrap(new byte[] { 1, 2, 3, 4, 5, 6 }));
            fail("Should not create flap header without 0x2a header");
        } catch (IllegalArgumentException e) {
        }
        FlapHeader header = new FlapHeader(ByteBlock.wrap(new byte[] { 0x2a, 9, 0, 120, 1, 2 }));
        assertEquals(9, header.getChannel());
        assertEquals(120, header.getSeqnum());
        assertEquals(258, header.getDataLength());
    }
