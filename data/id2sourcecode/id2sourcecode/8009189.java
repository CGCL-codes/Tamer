    public static String toString(InputStream inputStream) throws UnsupportedEncodingException, IOException {
        Reader reader = new InputStreamReader(inputStream);
        StringWriter writer = new StringWriter();
        copy(reader, writer);
        return writer.toString();
    }
