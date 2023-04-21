    public static void writeAllText(String text, OutputStream stream, Charset charset) {
        StringReader reader = new StringReader(text);
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(stream, charset));
        copyAll(reader, writer);
    }
