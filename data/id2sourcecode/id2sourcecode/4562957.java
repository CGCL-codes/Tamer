    public static HTTPResult get(String url, int timeout) throws GenericException {
        HttpURLConnection huc = null;
        try {
            huc = (HttpURLConnection) new URL(url).openConnection();
            huc.setReadTimeout(timeout * 1000);
            huc.setAllowUserInteraction(false);
            huc.setUseCaches(false);
            huc.setRequestProperty("User-Agent", USER_AGENT);
            huc.setRequestMethod("GET");
            huc.setDoInput(true);
            huc.connect();
            return getHTTPResult(huc);
        } catch (Exception e) {
            throw new GenericException(e, Errors.IO_HTTP_DOWNLOAD, url, "?");
        } finally {
            if (huc != null) huc.disconnect();
        }
    }
