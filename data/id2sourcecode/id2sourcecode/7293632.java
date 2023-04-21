    public AndroidURLConnection(URL url) throws IOException {
        this(url.openConnection());
    }
