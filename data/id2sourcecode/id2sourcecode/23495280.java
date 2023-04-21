    protected HttpRequest(IGGConfiguration configuration) throws IOException {
        if (configuration == null) throw new IllegalArgumentException("configuration cannot be null");
        m_ggconfiguration = configuration;
        URL url = new URL(getURL());
        m_huc = (HttpURLConnection) url.openConnection();
        m_huc.setRequestMethod("POST");
        m_huc.setDoInput(true);
        if (wannaWrite()) {
            m_huc.setDoOutput(true);
        }
        m_huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        m_huc.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0; Windows 98)");
    }
