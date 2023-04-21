    public static String httpPost(String urlReq, String[][] attribs, String userAgent) throws Exception {
        StringBuffer result = new StringBuffer();
        DefaultHttpClient client = new DefaultHttpClient();
        ArrayList<BasicNameValuePair> attributeList = new ArrayList<BasicNameValuePair>();
        for (int i = 0; i < attribs.length; i++) {
            attributeList.add(new BasicNameValuePair(attribs[i][0], attribs[i][1]));
        }
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(attributeList);
        HttpPost method = new HttpPost(new URI(urlReq));
        method.setHeader("user-agent", userAgent);
        method.setEntity(entity);
        result.append("REQUEST=").append(method.getRequestLine()).append("\n");
        Header[] header = method.getAllHeaders();
        for (int i = 0; i < header.length; i++) result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
        HttpResponse res = client.execute(method);
        StatusLine status = res.getStatusLine();
        result.append("RESPONSE: ").append(status.getStatusCode() + " " + status.getReasonPhrase());
        header = res.getAllHeaders();
        for (int i = 0; i < header.length; i++) result.append(header[i].getName() + "='" + header[i].getValue() + "'\n");
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
