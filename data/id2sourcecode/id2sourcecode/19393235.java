    public static InputStream getRequest(String path) throws Exception {
        HttpGet httpGet = new HttpGet(path);
        HttpResponse httpResponse = sClient.execute(httpGet);
        Header[] hs = httpResponse.getAllHeaders();
        boolean a = false;
        for (Header h : hs) {
            if (h.getValue().contains("gzip")) {
                a = true;
            }
        }
        if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            BufferedHttpEntity bufHttpEntity = new BufferedHttpEntity(httpResponse.getEntity());
            InputStream is = bufHttpEntity.getContent();
            GZIPInputStream iss = new GZIPInputStream(is);
            return iss;
        } else {
            return null;
        }
    }
