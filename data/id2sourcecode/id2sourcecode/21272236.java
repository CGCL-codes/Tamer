    public URLConnection getConnection(String urlData) throws Exception {
        URL url = new URL(urlData);
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", "OpenForum Wiki");
        return connection;
    }
