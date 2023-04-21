    private void checkMD1(MessageDigest md1, Provider p) throws Exception {
        byte[] b = { 1, 2, 3, 4, 5 };
        assertTrue("getInstance() failed", md1 instanceof MyMessageDigest1);
        assertEquals("getProvider() failed", p, md1.getProvider());
        assertEquals("getAlgorithm() failed", "ABC", md1.getAlgorithm());
        md1.update((byte) 1);
        md1.update(b, 1, 4);
        assertTrue("update failed", ((MyMessageDigest1) md1).runEngineUpdate1);
        assertTrue("update failed", ((MyMessageDigest1) md1).runEngineUpdate2);
        assertEquals("incorrect digest result", 0, md1.digest().length);
        assertEquals("getProvider() failed", 0, md1.digest(b, 2, 3));
        assertTrue("digest failed", ((MyMessageDigest1) md1).runEngineDigest);
        md1.reset();
        assertTrue("reset failed", ((MyMessageDigest1) md1).runEngineReset);
        assertEquals("getDigestLength() failed", 0, md1.getDigestLength());
        try {
            md1.clone();
            fail("No expected CloneNotSupportedException");
        } catch (CloneNotSupportedException e) {
        }
    }
