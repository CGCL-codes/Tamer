    @Test
    public void test02_ok_simple() throws Exception {
        DefaultHttpClient client = new DefaultHttpClient();
        try {
            HttpPost httpost = new HttpPost(xlsURL);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("wsName", "getGlobalVulnCountEvolution"));
            nvps.add(new BasicNameValuePair("p1", "chart"));
            nvps.add(new BasicNameValuePair("p2", "week"));
            nvps.add(new BasicNameValuePair("p3", "all"));
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = client.execute(httpost);
            assertEquals("failed code for ", 200, response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            assertNotNull("page empty for ", entity);
        } finally {
            client.getConnectionManager().shutdown();
        }
    }
