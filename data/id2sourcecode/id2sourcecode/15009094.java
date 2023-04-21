    public void processJsonRequest(Reader reader, Writer writer) throws IOException {
        new JsonRequestProcessor(this.registry, this.dispatcher, this.globalConfiguration).process(reader, writer);
    }
