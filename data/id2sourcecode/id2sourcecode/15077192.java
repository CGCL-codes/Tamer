    public static void main(String[] args) throws ClientProtocolException, IOException {
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://s.goso.cn/");
        httpget.addHeader("X-Forwarded-For", "211.100.48.140");
        HttpResponse response = httpclient.execute(httpget);
        StatusLine sl = response.getStatusLine();
        System.out.println(sl.getProtocolVersion().getProtocol() + " " + sl.getStatusCode() + " " + sl.getReasonPhrase());
        HttpEntity entity = response.getEntity();
        if (entity != null) {
            InputStream instream = entity.getContent();
            int l;
            byte[] tmp = new byte[2048];
            while ((l = instream.read(tmp)) != -1) {
                System.out.print(new String(tmp, 0, l));
            }
            instream.close();
        }
    }
