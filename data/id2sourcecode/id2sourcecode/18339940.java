    public static String post(Context ctxt, DefaultHttpClient client, String url, Map<String, String> hash) throws ClientProtocolException, IOException {
        HttpPost method = new HttpPost(url);
        Set<Entry<String, String>> entry = hash.entrySet();
        Entry<String, String> obj;
        Iterator<Entry<String, String>> iterator = entry.iterator();
        ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
        while (iterator.hasNext()) {
            obj = iterator.next();
            String key = obj.getKey();
            String val = obj.getValue();
            params.add(new BasicNameValuePair(key, val));
        }
        method.setEntity(new UrlEncodedFormEntity(params, org.apache.http.protocol.HTTP.UTF_8));
        HttpParams httpParams = new BasicHttpParams();
        HttpProtocolParams.setVersion(httpParams, HttpVersion.HTTP_1_1);
        HttpProtocolParams.setContentCharset(httpParams, org.apache.http.protocol.HTTP.UTF_8);
        HttpProtocolParams.setUseExpectContinue(httpParams, false);
        method.setParams(httpParams);
        method.removeHeaders("Expect");
        HttpResponse resp = client.execute(method);
        if (resp.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("error " + resp.getStatusLine().getStatusCode());
        }
        InputStream in = resp.getEntity().getContent();
        StringWriter sw = new StringWriter();
        int c = in.read();
        while (c != -1) {
            sw.write(c);
            c = in.read();
        }
        return sw.toString();
    }
