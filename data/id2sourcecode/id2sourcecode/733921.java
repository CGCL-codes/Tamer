    public static void testClient() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet("http://cnota.cn/g.jsp?18500_895");
        HttpResponse response = client.execute(request);
        System.out.println(response.getStatusLine());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            System.out.println(EntityUtils.toString(entity));
        }
        request.abort();
        client.getConnectionManager().shutdown();
    }
