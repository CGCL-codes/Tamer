    @Test(groups = { "functest", "digest" }, dataProvider = "testdataconv")
    public void testDigestStream(final DigestAlgorithm digest, final Converter converter) throws Exception {
        logger.info("Testing digest stream handling for {} using converter {}", digest, converter);
        final InputStream in1 = getClass().getResourceAsStream(BIG_FILE_PATH);
        final InputStream in2 = getClass().getResourceAsStream(BIG_FILE_PATH);
        try {
            final DigestAlgorithm copy = new DigestAlgorithm(digest.getDigest());
            final String refHash = digest.digest(in1, converter);
            final String testHash = copy.digest(in2, converter);
            AssertJUnit.assertEquals(refHash, testHash);
        } finally {
            if (in1 != null) {
                in1.close();
            }
            if (in2 != null) {
                in2.close();
            }
        }
    }
