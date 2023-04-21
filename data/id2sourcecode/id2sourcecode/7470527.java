    public String post(String url, String postStr, String encoding) throws Throwable {
        StringEntity entity = new StringEntity(postStr, encoding);
        HttpPost httpPost = new HttpPost(url);
        httpPost.setEntity(entity);
        HttpResponse response = client.execute(httpPost);
        String entuity = EntityUtils.toString(response.getEntity());
        return entuity;
    }
