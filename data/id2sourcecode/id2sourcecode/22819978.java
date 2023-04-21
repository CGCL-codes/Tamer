    protected HttpRequest(final IGGConfiguration configuration) throws IOException {
        if (configuration == null) {
            throw new IllegalArgumentException("configuration cannot be null");
        }
        ggConfiguration = configuration;
        final URL url = new URL(getURL());
        huc = (HttpURLConnection) url.openConnection();
        huc.setConnectTimeout(ggConfiguration.getSocketTimeoutInMiliseconds());
        huc.setReadTimeout(ggConfiguration.getSocketTimeoutInMiliseconds());
        huc.setRequestMethod("POST");
        huc.setDoInput(true);
        if (wannaWrite()) {
            huc.setDoOutput(true);
        }
        huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        huc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98)");
    }
