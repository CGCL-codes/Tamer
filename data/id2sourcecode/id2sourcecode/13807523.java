    public static String readAllText(InputStream stream, Charset charset) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, charset));
        StringWriter writer = new StringWriter();
        copyAll(reader, writer);
        return writer.toString();
    }
