    protected String getProcessedFile(String name, Action action) throws ParsingException, IOException {
        File file = fileResolver.resolve(name);
        InputStreamReader reader = new InputStreamReader(new FileInputStream(file), encoding);
        StringWriter writer = new StringWriter();
        try {
            process(reader, writer, action);
            return writer.toString();
        } finally {
            IOUtils.close(reader, null);
        }
    }
