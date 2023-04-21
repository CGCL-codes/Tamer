    public void testMD5DigestWrapper() {
        MD5DigestWrapper md5 = new MD5DigestWrapper();
        byte[] data = new byte[] { 'a' };
        byte[] digestExpected = md5digest("0cc175b9c0f1b6a831c399e269772661");
        md5.update(data);
        byte[] digest = md5.digest();
        assertEquals("md5 digest.length", 0x10, digest.length);
        assertEquals("md5 digest", digestExpected, digest);
        md5 = new MD5DigestWrapper();
        md5.update(new byte[] { 'a' });
        md5.update(new byte[] { 'b' });
        md5.update(new byte[] { 'c' });
        digestExpected = md5digest("900150983cd24fb0d6963f7d28e17f72");
        digest = md5.digest();
        assertEquals("md5 digest.length", 0x10, digest.length);
        assertEquals("md5 digest", digestExpected, digest);
    }
