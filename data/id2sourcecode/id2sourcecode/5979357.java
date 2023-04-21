    private Reader doCgiFormGet(String urlLoc) throws MalformedURLException, IOException {
        URL url = new URL(urlLoc);
        URLConnection connection = url.openConnection();
        Reader reader = new InputStreamReader(connection.getInputStream());
        return reader;
    }
