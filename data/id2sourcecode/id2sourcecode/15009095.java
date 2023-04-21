    public void processPollRequest(Reader reader, Writer writer, String pathInfo) throws IOException {
        new PollRequestProcessor(this.registry, this.dispatcher, this.globalConfiguration).process(reader, writer, pathInfo);
    }
