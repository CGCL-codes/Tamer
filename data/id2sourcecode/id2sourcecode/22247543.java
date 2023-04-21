    public static String readFully(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        copy(reader, writer, 1024);
        return writer.toString();
    }
