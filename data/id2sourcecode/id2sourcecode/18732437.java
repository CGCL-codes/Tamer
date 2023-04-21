    public String read(Reader reader) throws IOException {
        if (reader == null) throw new NullPointerException();
        final StringWriter writer = new StringWriter();
        try {
            IOUtils.readerToWriter(reader, writer);
            return writer.toString();
        } finally {
            IOUtils.closeProperly(reader);
            IOUtils.closeProperly(writer);
        }
    }
