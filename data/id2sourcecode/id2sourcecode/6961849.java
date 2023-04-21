    public void test_digest() {
        MyMessageDigest1 md = new MyMessageDigest1("ABC");
        assertEquals("incorrect result", 0, md.digest().length);
        assertTrue(md.runEngineDigest);
    }
