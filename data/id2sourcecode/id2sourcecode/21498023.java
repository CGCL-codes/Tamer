    public static int transfer(Reader reader, Writer writer) throws IOException {
        int totalChars = 0;
        char[] buffer = new char[16384];
        int charsRead;
        while ((charsRead = reader.read(buffer, 0, buffer.length)) > 0) {
            writer.write(buffer, 0, charsRead);
            totalChars += charsRead;
        }
        return totalChars;
    }
