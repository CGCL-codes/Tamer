    @Test(groups = { "functest", "digest" }, dataProvider = "testdataconv")
    public void testDigestConvert(final DigestAlgorithm digest, final Converter converter) throws Exception {
        logger.info("Testing digest output conversion for {}", digest);
        final DigestAlgorithm copy = new DigestAlgorithm(digest.getDigest());
        AssertJUnit.assertEquals(digest.digest(CLEARTEXT.getBytes(), converter), copy.digest(CLEARTEXT.getBytes(), converter));
    }
