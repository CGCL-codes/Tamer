    public byte[] getRawPair(String url, Map<String, String> headers, ArrayList<NameValuePair> parameters, int timeout) throws Exception {
        if (parameters != null && !url.contains("?")) {
            url += "?" + URLEncodedUtils.format(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
        } else if (parameters != null && url.contains("?")) {
            url += "&" + URLEncodedUtils.format(parameters, HTTP.DEFAULT_CONTENT_CHARSET);
        }
        HttpGet httpget = new HttpGet(url);
        if (headers != null) {
            for (String key : headers.keySet()) httpget.addHeader(key, headers.get(key));
        }
        System.out.println("URL " + url);
        HttpClient httpclient = getHttpClient(timeout);
        HttpResponse response = httpclient.execute(httpget);
        System.out.println("URL Executed");
        if (response == null) throw new Exception("Null response");
        if (response.getStatusLine().getStatusCode() != 200) throw new Exception("Server return code " + response.getStatusLine().getStatusCode() + " for url " + url);
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        response.getEntity().writeTo(outstream);
        return outstream.toByteArray();
    }
