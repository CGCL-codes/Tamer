    public void write(Writer writer) throws IOException {
        StringReader reader = null;
        try {
            reader = new StringReader(string);
            IOUtils.readerToWriter(reader, writer);
        } finally {
            IOUtils.closeProperly(writer);
            IOUtils.closeProperly(reader);
        }
    }
