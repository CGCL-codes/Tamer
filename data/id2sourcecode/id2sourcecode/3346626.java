    public static void readTo(Reader reader, Writer writer) throws IOException {
        char cbuf[] = new char[1024];
        int i = 0;
        while ((i = reader.read(cbuf, 0, 1024)) > 0) {
            writer.write(cbuf, 0, i);
        }
        writer.flush();
    }
