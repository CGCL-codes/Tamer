    public static void flow(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[DEFAULT_BUFFER_SIZE];
        flow(reader, writer, buf);
    }
