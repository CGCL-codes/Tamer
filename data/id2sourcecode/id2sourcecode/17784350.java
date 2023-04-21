    public static void transfer(Reader reader, Writer writer) throws IOException {
        char[] buf = new char[8192];
        int count = 0;
        while ((count = reader.read(buf)) >= 0) {
            writer.write(buf, 0, count);
        }
        writer.flush();
    }
