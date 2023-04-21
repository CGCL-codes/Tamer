    public final void testDigest02() {
        int i = 0;
        byte[] results = null;
        for (int n = 0; n < LENGTHS.length; n++) {
            for (int j = 0; j < LENGTHS[n]; j++) {
                md.update((byte) (j + 1));
            }
            results = md.digest();
            for (int j = 0; j < DIGESTLENGTH; j++) {
                assertEquals("results[j] != HASHTOCOMPARE[i][j] :: n=" + n + " j=" + j + " i=" + i, results[j], HASHTOCOMPARE[i][j]);
            }
            i++;
        }
    }
