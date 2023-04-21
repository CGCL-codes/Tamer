    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[8192];
        int n = 0;
        while ((n = reader.read(buf)) != -1) {
            writer.write(buf, 0, n);
        }
    }
