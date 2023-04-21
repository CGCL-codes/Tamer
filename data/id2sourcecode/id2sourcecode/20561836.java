    private static void copyIO(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[4096];
        int len;
        while ((len = reader.read(buffer)) >= 0) {
            writer.write(buffer, 0, len);
        }
    }
