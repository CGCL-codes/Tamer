    private void prefetch(String baseURL, String dest, String iuid) throws IOException {
        String url = baseURL + "&objectUID=" + iuid;
        try {
            URL wadourl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) wadourl.openConnection();
            if (conn.getResponseCode() != HttpServletResponse.SC_OK) {
                log.warn("Prefetch WADO URL failed:" + wadourl);
                throw new IOException(conn.getResponseCode() + ":" + conn.getResponseMessage());
            }
            if (dest != null) {
                export(dest, iuid, conn);
            }
        } catch (MalformedURLException e) {
            log.error("Prefetch request ignored: Malformed WADO URL! Need configuration change in wado-prefetch.xsl! url:" + url);
        }
    }
