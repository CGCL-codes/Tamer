    private void init() throws MalformedURLException, IOException {
        super.setParser(Parser.detectParser(url.openStream(), url));
        super.setIos(url.openStream());
    }
