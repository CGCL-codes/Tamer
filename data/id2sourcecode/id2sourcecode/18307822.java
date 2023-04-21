    public static String parseInputStream(InputStream in, Map contextMap) throws Exception {
        StringWriter writer = new StringWriter();
        InputStreamReader reader = new InputStreamReader(in);
        parse(reader, writer, contextMap);
        return writer.toString();
    }
