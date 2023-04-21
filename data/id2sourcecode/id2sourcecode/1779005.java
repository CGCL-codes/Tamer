    public byte[] getRaw(String url, Map<String, String> headers, Map<String, String> params, int timeout) throws Exception {
        if (params != null && !url.contains("?")) {
            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) parameters.add(new BasicNameValuePair(key, params.get(key)));
            url += "?" + URLEncodedUtils.format(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
        } else if (params != null && url.contains("?")) {
            ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) parameters.add(new BasicNameValuePair(key, params.get(key)));
            url += "&" + URLEncodedUtils.format(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
        }
        HttpGet httpget = new HttpGet(url);
        if (headers != null) {
            for (String key : headers.keySet()) httpget.addHeader(key, headers.get(key));
        }
        HttpClient httpclient = getHttpClient(timeout);
        HttpResponse response = httpclient.execute(httpget);
        if (response == null) throw new Exception("Null response");
        if (response.getStatusLine().getStatusCode() != 200) throw new Exception("Server return code " + response.getStatusLine().getStatusCode());
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        response.getEntity().writeTo(outstream);
        return outstream.toByteArray();
    }
