    public void test_onZ() throws Exception {
        MessageDigest originalDigest = (MessageDigest) (digest.clone());
        MessageDigest noChangeDigest = (MessageDigest) (digest.clone());
        DigestInputStream dis = new DigestInputStream(inStream, noChangeDigest);
        dis.on(false);
        int c = dis.read();
        assertEquals('T', c);
        assertTrue("MessageDigest changed even though processing was off", MessageDigest.isEqual(noChangeDigest.digest(), originalDigest.digest()));
        MessageDigest changeDigest = (MessageDigest) (digest.clone());
        dis = new DigestInputStream(inStream, digest);
        dis.on(true);
        c = dis.read();
        assertEquals('h', c);
        assertTrue("MessageDigest did not change with processing on", !MessageDigest.isEqual(digest.digest(), changeDigest.digest()));
    }
