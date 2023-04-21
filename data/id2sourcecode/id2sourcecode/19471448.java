    @Test
    public void test01_lang_default() throws Exception {
        String[] entities = { "admin", "audits", "asset", "esis", "explorer", "identity", "riskregister", "risks", "vulnerabilities" };
        for (String entity : entities) {
            DefaultHttpClient client = new DefaultHttpClient();
            try {
                HttpGet get = new HttpGet(xmlLangURLs + entity);
                HttpResponse response = client.execute(get);
                assertEquals("failed code for " + entity, 200, response.getStatusLine().getStatusCode());
                assertNotNull("page empty for " + entity, response.getEntity());
            } finally {
                client.getConnectionManager().shutdown();
            }
        }
    }
