    public static String readToString(Reader reader) throws IOException {
        StringWriter writer = new StringWriter();
        readAndWriteToWriter(reader, writer);
        return writer.toString();
    }
