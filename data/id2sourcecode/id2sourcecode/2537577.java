    public static String toString(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        writeAll(reader, writer);
        return writer.toString();
    }
