    public void decode(URL url) throws ApplicationException, IOException, NotWellFormedException {
        Parser parser = new ParserImpl();
        parser.setApplication(this);
        parser.parseDocument(new OpenEntity(url.openStream(), url.toExternalForm(), url));
    }
