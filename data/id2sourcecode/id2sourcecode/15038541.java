    public static void copyStream(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[8192];
        int len;
        while ((len = reader.read(buf)) > 0) {
            writer.write(buf, 0, len);
        }
        writer.flush();
    }
