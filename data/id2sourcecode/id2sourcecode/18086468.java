    public static void flow(Reader reader, Writer writer, char[] buf) throws IOException {
        int numRead;
        while ((numRead = reader.read(buf)) >= 0) {
            writer.write(buf, 0, numRead);
        }
    }
