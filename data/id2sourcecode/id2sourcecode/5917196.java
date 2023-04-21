    public void testEvdbMd5() {
        try {
            String done = EvdbUtils.digest("0689559111", "H0gwart$");
            final String knownResult = "ea230d9de20eaaa2d8ed286cc71a8442";
            Assert.assertEquals("The response did not match the known result", knownResult, done);
        } catch (UnsupportedEncodingException e) {
            Assert.fail("Encoding error: " + e.getMessage());
        }
    }
