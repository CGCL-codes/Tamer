    public static final StringBuffer readAll(Reader reader) throws IOException {
        final StringWriter writer = new StringWriter(1024 * 16);
        copy(writer, reader);
        return writer.getBuffer();
    }
