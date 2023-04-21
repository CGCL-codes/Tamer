    public static String format(String text) throws IOException {
        BufferedReader reader = new BufferedReader(new StringReader(text));
        StringWriter writer = new StringWriter();
        doFormat(reader, writer, "");
        return writer.getBuffer().toString();
    }
