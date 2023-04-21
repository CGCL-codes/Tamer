    public void testUnsupportedFeatures() throws Exception {
        TestURLRegistry.register("unsupp", new ByteArrayInputStream("".getBytes()));
        URL url = new URL("testurl:unsupp");
        URLConnection uc = url.openConnection();
        try {
            uc.getOutputStream();
            fail("Should have thrown exception");
        } catch (UnknownServiceException use) {
        }
        try {
            uc.getContent();
            fail("Should have thrown exception");
        } catch (Exception e) {
        }
        assertNull(uc.getHeaderField(0));
    }
