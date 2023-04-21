    public static String readAll(Reader reader) {
        StringWriter writer = new StringWriter();
        copyAll(reader, writer);
        return writer.toString();
    }
