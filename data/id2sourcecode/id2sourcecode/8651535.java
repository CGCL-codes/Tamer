    @Test(groups = { "functest", "digest" }, dataProvider = "testdataref")
    public void testHashCalculationAgainstReference(final DigestAlgorithm digest) throws Exception {
        logger.info("Testing {} calculation against reference value", digest);
        final String testHash = digest.digest(CLEARTEXT.getBytes("ASCII"), new HexConverter());
        AssertJUnit.assertEquals(REFERENCE_HASHES.get(digest.getAlgorithm()), testHash);
    }
