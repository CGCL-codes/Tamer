    public static void copy(Reader reader, Writer writer) throws IOException {
        int charsRead;
        char[] buffer = new char[2048];
        while ((charsRead = reader.read(buffer)) > 0) {
            writer.write(buffer, 0, charsRead);
        }
        writer.flush();
        reader.close();
    }
