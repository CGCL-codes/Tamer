    @Override
    public void disconnect() throws ConnectionException {
        if (!authenticated) throw new IllegalStateException("Must be authenticated to disconnect.");
        URL url;
        try {
            url = new URL(getProperty("disconnect.url"));
        } catch (NullPointerException ex) {
            throw new ConnectionException("No azet disconnect url was found.", ex);
        } catch (MalformedURLException ex) {
            throw new ConnectionException("Azet disconnect url is malformed.", ex);
        }
        try {
            HttpURLHandler urlHandler = new HttpURLHandler(url);
            urlHandler.getInputStream();
        } catch (IOException ex) {
            throw new ConnectionException("Unable to read from/write to azet disconnect url", ex);
        }
    }
