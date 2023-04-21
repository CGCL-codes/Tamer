    public static void copyStream(Reader reader, Writer writer) throws IOException {
        int len;
        char[] buffer = new char[1024];
        while ((len = reader.read(buffer)) >= 0) {
            writer.write(buffer, 0, len);
        }
    }
