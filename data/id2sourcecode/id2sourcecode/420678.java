    public static final void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File("d:\\tomcat.keystore"));
            try {
                trustStore.load(instream, "123456".toCharArray());
            } finally {
                try {
                    instream.close();
                } catch (Exception ignore) {
                }
            }
            SSLSocketFactory socketFactory = new SSLSocketFactory(trustStore);
            Scheme sch = new Scheme("https", 8443, socketFactory);
            httpclient.getConnectionManager().getSchemeRegistry().register(sch);
            HttpGet httpget = new HttpGet("https://192.168.0.117:8443");
            System.out.println("executing request" + httpget.getRequestLine());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream input = entity.getContent();
            String contentStr = IOUtils.toString(input);
            System.out.println(contentStr);
        } finally {
            httpclient.getConnectionManager().shutdown();
        }
    }
