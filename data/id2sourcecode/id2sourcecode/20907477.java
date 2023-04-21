    public void testErrorOnConnect() throws Exception {
        TestURLRegistry.register();
        URL url = new URL("testurl:errorOnConnect");
        try {
            url.openStream();
            fail("Should have thrown exception");
        } catch (IOException ioex) {
            assertTrue(ioex.getMessage().indexOf("Simulated") > -1);
        }
    }
