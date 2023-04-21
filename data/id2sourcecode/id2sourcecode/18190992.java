    public void generate(Reader reader, Writer writer) throws TransformerException {
        if (config == null) {
            throw new IllegalStateException("CodeSnippetGenerator has not been initialized.");
        }
        HTMLGenerator htmlGenerator = createHTMLGenerator(config, reader);
        htmlGenerator.write(writer);
    }
