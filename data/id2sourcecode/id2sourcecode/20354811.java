    public void replace(Reader reader, Writer writer, Map<String, String> variables) throws IOException {
        replace(reader, writer, new MapVariableResolver(variables));
    }
