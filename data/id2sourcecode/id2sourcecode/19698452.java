    @Test(expected = IllegalArgumentException.class)
    public void testBadDigest() {
        sequence = null;
        trypsin.digest(sequence);
    }
