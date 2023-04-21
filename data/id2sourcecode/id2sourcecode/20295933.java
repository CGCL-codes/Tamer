    @Test
    public void testJdkURLConnection() throws Exception {
        dispatcher = EmbeddedContainer.start().getDispatcher();
        dispatcher.getRegistry().addPerRequestResource(SimpleResource.class);
        try {
            URL url = createURL("/simple");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            @SuppressWarnings("unused") Object obj = conn.getContent();
        } finally {
            EmbeddedContainer.stop();
        }
    }
