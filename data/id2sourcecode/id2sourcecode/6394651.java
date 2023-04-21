    public static InputStream openFileOrUrl(String urlString) throws IOException {
        URL url;
        try {
            url = new URL(urlString);
        } catch (MalformedURLException e) {
            return new FileInputStream(urlString);
        }
        URLConnection conn = url.openConnection();
        conn.setConnectTimeout(timeout);
        conn.setReadTimeout(timeout);
        return conn.getInputStream();
    }
