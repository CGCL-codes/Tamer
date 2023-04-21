    public final void testDigest03() {
        byte[] bytes1 = null;
        byte[] bytes2 = null;
        for (int i = 1; i < LENGTH; i++) {
            byte[] b = new byte[i];
            for (int j = 0; j < b.length; j++) {
                b[j] = (byte) j;
            }
            md.update(b, 0, b.length);
            bytes1 = md.digest();
            md.update(b, 0, b.length);
            bytes2 = md.digest();
            assertEquals("bytes1.length != bytes2.length", bytes1.length, bytes2.length);
            for (int j = 0; j < DIGESTLENGTH; j++) {
                assertEquals("no equality for i=" + i + " j=" + j, bytes1[j], bytes2[j]);
            }
        }
    }
