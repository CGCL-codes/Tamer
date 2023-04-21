    public final void testDigestbyteArrayintint03() throws DigestException {
        byte[] bytes1 = new byte[DIGESTLENGTH];
        byte[] bytes2 = new byte[DIGESTLENGTH];
        for (int n = 1; n < LENGTH; n++) {
            byte[] b = new byte[n];
            for (int j = 0; j < b.length; j++) {
                b[j] = (byte) j;
            }
            md.update(b, 0, b.length);
            md.digest(bytes1, 0, DIGESTLENGTH);
            md.update(b, 0, b.length);
            md.digest(bytes2, 0, DIGESTLENGTH);
            assertEquals("bytes1.length != bytes2.length", bytes1.length, bytes2.length);
            for (int j = 0; j < DIGESTLENGTH; j++) {
                assertEquals("no equality for j=" + j, bytes1[j], bytes2[j]);
            }
        }
    }
