    private Properties loadLocaleEntries(URL url) {
        Properties entries = new Properties();
        try {
            InputStream is = url.openStream();
            entries.load(is);
        } catch (IOException e) {
            return entries;
        }
        return entries;
    }
