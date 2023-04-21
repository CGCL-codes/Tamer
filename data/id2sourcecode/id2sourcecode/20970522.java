    public static String readFully(InputStream input) throws IOException {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(input);
        copy(reader, writer, 1024);
        return writer.toString();
    }
