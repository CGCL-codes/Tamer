    public static String httpGet(String urlReq, String userAgent) throws Exception {
        StringBuffer result = new StringBuffer();
        DefaultHttpClient client = new DefaultHttpClient();
        client.setRedirectHandler(new RedirectHandler() {

            @Override
            public boolean isRedirectRequested(HttpResponse response, HttpContext context) {
                return false;
            }

            @Override
            public URI getLocationURI(HttpResponse response, HttpContext context) throws ProtocolException {
                return null;
            }
        });
        HttpGet method = new HttpGet(new URI(urlReq));
        method.setHeader("user-agent", userAgent);
        HttpResponse res = client.execute(method);
        StatusLine status = res.getStatusLine();
        result.append(status.getStatusCode() + " " + status.getReasonPhrase());
        Header[] header = res.getAllHeaders();
        for (int i = 0; i < header.length; i++) {
            result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
        }
        int length = Math.min(30000, (int) res.getEntity().getContentLength());
        if (length <= 0) length = 30000;
        InputStream is = res.getEntity().getContent();
        byte[] buf = new byte[length];
        int cnt = is.read(buf);
        while ((cnt > 0) && (length > 0)) {
            result.append(new String(buf, 0, cnt));
            length -= cnt;
            cnt = is.read(buf);
        }
        return result.toString();
    }
