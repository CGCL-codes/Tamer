    public static InputStream getInputStream(URL a_url, boolean a_useProxyCache) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) a_url.openConnection();
        if (!a_useProxyCache) {
            connection.setRequestProperty("Cache-Control", "no-cache");
        }
        return connection.getInputStream();
    }
