    private String getXml(String url) {
        String results = null;
        try {
            url = _metadataSearchPrefix + url;
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                long len = entity.getContentLength();
                if (len != -1 && len < 2048) {
                    results = EntityUtils.toString(entity);
                } else {
                }
            }
        } catch (IOException iox) {
            iox.printStackTrace();
            _log.error("unable to process metadata from spidrvo");
        }
        return results;
    }
