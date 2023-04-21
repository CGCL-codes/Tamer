    public static String get(Context ctxt, DefaultHttpClient client, String url) throws ClientProtocolException, IOException {
        if (client == null) return null;
        HttpGet method = new HttpGet(url);
        HttpResponse resp = client.execute(method);
        if (resp.getStatusLine().getStatusCode() != 200) {
            throw new RuntimeException("error " + resp.getStatusLine().getStatusCode());
        } else {
            InputStream in = resp.getEntity().getContent();
            InputStreamReader r = new InputStreamReader(in, "UTF-8");
            StringWriter sw = new StringWriter();
            int c = r.read();
            while (c != -1) {
                sw.write(c);
                c = r.read();
            }
            return sw.toString();
        }
    }
