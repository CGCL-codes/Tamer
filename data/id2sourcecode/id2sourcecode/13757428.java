    public String doGet(String url) {
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String str = convertStreamToString(entity.getContent());
            return str;
        } catch (IOException e) {
            return null;
        }
    }
