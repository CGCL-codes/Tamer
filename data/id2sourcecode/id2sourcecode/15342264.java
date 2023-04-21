    public static void copyReaderToWriter(Reader reader, Writer writer, boolean closeWriter) throws IOException {
        char[] c = new char[BUFFER_SIZE];
        int n;
        while ((n = reader.read(c)) != -1) {
            writer.write(c, 0, n);
        }
        reader.close();
        writer.flush();
        if (closeWriter) {
            writer.close();
        }
    }
