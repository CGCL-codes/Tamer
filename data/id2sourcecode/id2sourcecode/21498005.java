    public static String getContentOfURI(String uri) throws IOException {
        Reader reader = getReaderForURI(uri);
        StringWriter writer = new StringWriter();
        transfer(reader, writer);
        return writer.toString();
    }
