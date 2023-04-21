    @Override
    public int loadCache(URL url) throws IOException {
        if (url == null) {
            url = new URL(IEEE_OUI_DATABASE_PATH);
        }
        return readOuisFromRawIEEEDb(new BufferedReader(new InputStreamReader(url.openStream())));
    }
