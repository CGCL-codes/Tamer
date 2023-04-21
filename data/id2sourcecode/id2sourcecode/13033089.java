    public void write(Writer writer) {
        StringReader reader = new StringReader(str);
        IOUtils.pipe(reader, writer);
    }
