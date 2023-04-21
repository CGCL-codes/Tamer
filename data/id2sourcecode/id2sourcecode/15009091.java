    public void processSimpleFormPostRequest(Reader reader, Writer writer) throws IOException {
        new SimpleFormPostRequestProcessor(this.registry, this.dispatcher, this.globalConfiguration).process(reader, writer);
    }
