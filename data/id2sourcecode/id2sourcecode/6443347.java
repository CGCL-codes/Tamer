    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[COPY_BUF_SIZE];
        int read = 0;
        while ((read = reader.read(buf)) != -1) {
            writer.write(buf, 0, read);
        }
    }
