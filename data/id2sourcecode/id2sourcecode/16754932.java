    public static InputStream getInputStream(URL url) throws IOException {
        final URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "Mozilla/4.0");
        return new BufferedInputStream(connection.getInputStream());
    }
