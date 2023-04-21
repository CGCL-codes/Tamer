    public static void copy(Reader reader, Writer writer) throws IOException {
        char[] buffer = new char[CHAR_COPY_BUFFER_SIZE];
        int numChars;
        while ((numChars = reader.read(buffer)) > 0) writer.write(buffer, 0, numChars);
    }
