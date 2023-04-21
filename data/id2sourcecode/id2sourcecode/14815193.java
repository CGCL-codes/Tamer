    private static void readerToWriter(Reader reader, Writer writer) throws IOException {
        int numChars;
        final int bufferSize = 16384;
        char[] buffer = new char[bufferSize];
        while ((numChars = reader.read(buffer)) != -1) {
            if (numChars > 0) writer.write(buffer, 0, numChars);
        }
    }
