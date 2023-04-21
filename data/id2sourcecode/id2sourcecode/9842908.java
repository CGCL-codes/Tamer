    @Test
    public void urlTest() throws ClientProtocolException, IOException {
        String url = "http://www.pharmnet.com.cn/sms/index1.html";
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        String urlContent = IOUtils.toString(entity.getContent());
        System.out.println(urlContent);
    }
