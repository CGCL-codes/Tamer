    public static String toString(String encoding, InputStream inputStream) throws UnsupportedEncodingException, IOException {
        Reader reader = new InputStreamReader(inputStream, encoding);
        StringWriter writer = new StringWriter();
        copy(reader, writer);
        return writer.toString();
    }
