    @Override
    public List<Buddy> getBuddies() throws ConnectionException {
        if (!authenticated) throw new IllegalStateException("Must be authenticated to obtain buddy list.");
        URL url;
        try {
            url = new URL(getProperty("buddyList.url"));
        } catch (NullPointerException ex) {
            throw new ConnectionException("No azet buddy list url was found.", ex);
        } catch (MalformedURLException ex) {
            throw new ConnectionException("Azet buddy list url is malformed", ex);
        }
        try {
            HttpURLHandler urlHandler = new HttpURLHandler(url);
            return buddyListHandler.parse(urlHandler.getInputStream());
        } catch (IOException ex) {
            throw new ConnectionException("Unable to read from/write to azet buddy list url", ex);
        }
    }
