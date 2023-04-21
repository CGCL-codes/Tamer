    public String urlRead(String urlString) throws Throwable {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.connect();
        InputStream is = connection.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        return readAll(reader);
    }
