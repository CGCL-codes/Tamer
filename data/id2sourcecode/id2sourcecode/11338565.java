    @Test
    public void testSaltedSecureDigest() {
        IDigester secureDigester = DigesterFactory.getInstance().getSaltedSecureDigester();
        secureDigester.digest("kapil");
    }
