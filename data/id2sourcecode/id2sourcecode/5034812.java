    public final void testDigestbyteArrayintint02() throws DigestException {
        byte[] bytes = null;
        int i = 0;
        for (int n = 0; n < LENGTHS.length; n++) {
            byte[] results = new byte[DIGESTLENGTH];
            bytes = new byte[LENGTHS[n]];
            for (int j = 0; j < bytes.length; j++) {
                bytes[j] = (byte) (j + 1);
            }
            md.update(bytes, 0, bytes.length);
            md.digest(results, 0, DIGESTLENGTH);
            for (int j = 0; j < DIGESTLENGTH; j++) {
                assertEquals("results[j] != HASHTOCOMPARE[i][j] :: " + " n=" + n + " j=" + j + " i=" + i, results[j], HASHTOCOMPARE[i][j]);
            }
            i++;
        }
    }
