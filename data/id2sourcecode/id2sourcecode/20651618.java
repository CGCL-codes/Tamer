    private LineNumberReader createReaderFromUrl(URL url) throws IOException {
        return new LineNumberReader(new BufferedReader(new InputStreamReader(url.openStream())));
    }
