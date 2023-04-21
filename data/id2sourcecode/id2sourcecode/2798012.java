    private void checkMD2(MessageDigest md2, Provider p) throws Exception {
        byte[] b = { 1, 2, 3, 4, 5 };
        assertEquals("getProvider() failed", p, md2.getProvider());
        assertEquals("getAlgorithm() failed", "CBA", md2.getAlgorithm());
        md2.update((byte) 1);
        md2.update(b, 1, 3);
        assertTrue("update failed", MyMessageDigest2.runEngineUpdate1);
        assertTrue("update failed", MyMessageDigest2.runEngineUpdate2);
        assertEquals("incorrect digest result", 0, md2.digest().length);
        assertEquals("getProvider() failed", 0, md2.digest(b, 2, 3));
        assertTrue("digest failed", MyMessageDigest2.runEngineDigest);
        md2.reset();
        assertTrue("reset failed", MyMessageDigest2.runEngineReset);
        assertEquals("getDigestLength() failed", 0, md2.getDigestLength());
        try {
            md2.clone();
            fail("No expected CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {
        }
    }
