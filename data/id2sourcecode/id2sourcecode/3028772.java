    public PronounceableFSM(URL url, boolean scanFromFront) throws IOException {
        this.scanFromFront = scanFromFront;
        InputStream is = url.openStream();
        loadText(is);
        is.close();
    }
