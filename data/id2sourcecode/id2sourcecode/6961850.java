    public void test_digestLB$() {
        MyMessageDigest1 md = new MyMessageDigest1("ABC");
        byte[] b = { 1, 2, 3, 4, 5 };
        assertEquals("incorrect result", 0, md.digest(b).length);
        assertTrue(md.runEngineDigest);
    }
