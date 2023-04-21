    public void transform(Reader reader, Writer writer) throws IOException {
        transform(new ElementReader(reader), writer);
    }
