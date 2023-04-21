    public SAXReader(GraphSystem graphSystem, URL url) throws IOException {
        this.graphSystem = graphSystem;
        inputSource = new InputSource(url.openStream());
        inputSource.setSystemId(url.toString());
        createLexicalHandler();
    }
