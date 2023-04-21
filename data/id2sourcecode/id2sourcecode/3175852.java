    private static void copy(Reader reader, Writer writer) throws IOException {
        int c;
        while ((c = reader.read()) != -1) writer.write(c);
    }
