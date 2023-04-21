    public Object execute(String backendAddress, int backendPort, ProxyRequest request, String remoteAddr) throws org.apache.http.client.ClientProtocolException, IOException {
        org.apache.http.impl.client.DefaultHttpClient httpclient = new org.apache.http.impl.client.DefaultHttpClient();
        org.apache.http.HttpHost target = new org.apache.http.HttpHost(backendAddress, backendPort, "http");
        HttpProxyRequest proxyRequest = (HttpProxyRequest) request;
        org.apache.http.client.methods.HttpGet httpget = new org.apache.http.client.methods.HttpGet(proxyRequest.getQuery());
        boolean forwardedfor = false;
        for (Entry<String, String> en : proxyRequest.getHeaders()) {
            String key = en.getKey();
            if (key.equals("Connection")) {
                httpget.addHeader(key, "Close");
            } else if (key.equals("X-Forwarded-For")) {
                forwardedfor = true;
                httpget.addHeader(key, en.getValue());
            } else {
                httpget.addHeader(key, en.getValue());
            }
        }
        if (!forwardedfor) {
            httpget.addHeader("X-Forwarded-For", remoteAddr);
        }
        org.apache.http.HttpResponse response = httpclient.execute(target, httpget);
        return convert(response);
    }
