    private Preferences(URL url) throws IOException {
        prefs = new Properties();
        if (url != null && prefs != null) {
            InputStream inStream = url.openStream();
            if (inStream == null) {
                throw new IOException("open stream from URL (" + url.toString() + ")");
            } else {
                prefs.load(inStream);
            }
        }
    }
