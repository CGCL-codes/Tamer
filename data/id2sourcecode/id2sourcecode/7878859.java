    private boolean needToDownload(String url, String localFilePath) {
        File f = new File(localFilePath);
        if (!f.exists()) {
            return true;
        }
        Date fileLastModifed = new Date(f.lastModified());
        LOGGER.info("File modified at " + fileLastModifed);
        LOGGER.info("Check URL " + url);
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet httpRequest = new HttpGet(url);
        try {
            HttpResponse httpResponse = httpClient.execute(httpRequest);
            final int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode == 200) {
                Header h = httpResponse.getFirstHeader("Last-Modified");
                Date urlLastModifed = DateUtils.parseDate(h.getValue());
                LOGGER.info("urlLastModifed : " + urlLastModifed);
                if (urlLastModifed.after(fileLastModifed)) {
                    return true;
                }
            } else {
                LOGGER.warning("Bad response code =" + statusCode);
            }
        } catch (Throwable e) {
            LOGGER.error(e);
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return false;
    }
