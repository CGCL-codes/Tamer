    protected static String postData(String host, List<NameValuePair> nameValuePairs) throws ClientProtocolException, IOException {
        HttpParams httpParameters = new BasicHttpParams();
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(host);
        httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();
        String resp = EntityUtils.toString(resEntity, HTTP.UTF_8);
        String respEncoded = new String(resp.getBytes("Cp1251"), HTTP.UTF_8);
        return respEncoded;
    }
