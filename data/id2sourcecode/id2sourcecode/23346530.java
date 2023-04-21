    public Resolver(URL url) throws IOException {
        inputSource = new InputSource(url.openStream());
    }
