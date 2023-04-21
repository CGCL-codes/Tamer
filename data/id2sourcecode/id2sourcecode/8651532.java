    @Test(groups = { "functest", "digest" }, dataProvider = "testdata")
    public void testDigest(final DigestAlgorithm digest, final byte[] salt) throws Exception {
        logger.info("Testing digest algorithm {}", digest);
        final DigestAlgorithm copy = new DigestAlgorithm(digest.getDigest());
        if (salt != null) {
            digest.setSalt(salt);
            copy.setSalt(salt);
        }
        AssertJUnit.assertEquals(digest.digest(CLEARTEXT.getBytes()), copy.digest(CLEARTEXT.getBytes()));
    }
