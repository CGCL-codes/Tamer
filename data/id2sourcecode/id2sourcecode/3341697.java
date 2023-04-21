    public URLInputAdapter(URL url) throws IOException {
        super(new BufferedReader(new InputStreamReader(url.openStream())));
    }
