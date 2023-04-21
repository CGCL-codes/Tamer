    public long getSize(String url) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);
            long length = response.getEntity().getContentLength();
            LOGGER.log(Level.INFO, "File size found = {0}", length);
            if (length < 0) {
                LOGGER.info("length < 0 , not setting");
            } else {
                return length;
            }
            request.abort();
        } catch (Exception any) {
            LOGGER.log(Level.INFO, "", any);
        }
        return -1;
    }
