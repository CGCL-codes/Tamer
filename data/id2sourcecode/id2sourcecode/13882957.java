    private static void writeCharacters(Reader reader, Writer writer) throws IOException {
        try {
            char[] buffer = new char[4096];
            int length;
            while ((length = reader.read(buffer)) >= 0) {
                writer.write(buffer, 0, length);
            }
        } finally {
            writer.close();
            writer.close();
        }
    }
