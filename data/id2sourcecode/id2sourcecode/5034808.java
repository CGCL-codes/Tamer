    public final void testDigest01() {
        byte[] bytes = null;
        for (int i = 0; i < LENGTH; i++) {
            byte[] b = new byte[i];
            for (int j = 0; j < b.length; j++) {
                b[j] = (byte) j;
            }
            md.update(b, 0, b.length);
            bytes = md.digest();
            assertEquals("length of digest != DIGESTLENGTH", bytes.length, DIGESTLENGTH);
            if ((i & 1) == 0) {
                md.reset();
            }
        }
    }
