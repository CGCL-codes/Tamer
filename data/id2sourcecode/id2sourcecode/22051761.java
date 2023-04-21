    public static String toString(String path) throws IOException {
        StringWriter writer = new StringWriter();
        readFileAndWriteToWriter(path, writer);
        return writer.toString();
    }
