    @Test
    public void test03_fail_bad_entity() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(xmlLangURLs + "foobar");
        HttpResponse response = client.execute(get);
        try {
            assertNotNull("page empty", response.getEntity());
            String content = EntityUtils.toString(response.getEntity());
            assertTrue(content.contains("error"));
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
