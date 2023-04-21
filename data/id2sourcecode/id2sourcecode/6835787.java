    public void testDigest() throws Exception {
        Config config = ConfigDigester.digest("flashgatekeeper.xml");
        assertNotNull(config);
        log.info("Created \n" + config);
    }
