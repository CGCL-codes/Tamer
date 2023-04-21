    @Test
    public void test04_fail_bad_company() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet(xmlLangURLs + "admin&company=foobar");
        HttpResponse response = client.execute(get);
        try {
            assertNotNull("page empty", response.getEntity());
            String content = EntityUtils.toString(response.getEntity());
            assertTrue(content.contains("error"));
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
