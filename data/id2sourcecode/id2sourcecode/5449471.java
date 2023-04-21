    public void testGetClass() throws Exception {
        final SimpleRmiHttpServer server = new SimpleRmiHttpServer(TEST_PORT);
        try {
            final URL url = new URL(URL_BASE + "net/sf/openrds/IMainNode.class");
            assertInputStreamEquals(this.getClass().getResourceAsStream("IMainNode.class"), new BufferedInputStream(url.openStream()));
        } finally {
            server.stopRunning(true);
        }
    }
