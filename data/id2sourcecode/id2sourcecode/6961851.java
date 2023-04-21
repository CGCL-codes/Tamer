    public void test_digestLB$LILI() throws Exception {
        MyMessageDigest1 md = new MyMessageDigest1("ABC");
        byte[] b = { 1, 2, 3, 4, 5 };
        assertEquals("incorrect result", 0, md.digest(b, 2, 3));
        assertTrue("digest failed", md.runEngineDigest);
        md = new MyMessageDigest1();
        final byte[] bytes = new byte[] { 2, 4, 1 };
        try {
            md.digest(null, 0, 1);
            fail("No expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            md.digest(bytes, 0, bytes.length + 1);
            fail("No expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        try {
            md.digest(bytes, Integer.MAX_VALUE, 1);
            fail("No expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
        }
        final int offset = -1;
        final int len = -1;
        final int status = 33;
        md = new MyMessageDigest1("ABC") {

            @Override
            public int engineDigest(byte[] arg0, int arg1, int arg2) {
                assertSame("buf", bytes, arg0);
                assertEquals("offset", offset, arg1);
                assertEquals("len", len, arg2);
                return status;
            }
        };
        assertEquals("returned status", status, md.digest(bytes, offset, len));
    }
