    public Token request(String code) {
        Token token = null;
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpGet = createGet(code);
        try {
            HttpResponse response = httpclient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                token = toXml(EntityUtils.toString(entity));
                System.out.println();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return token;
    }
