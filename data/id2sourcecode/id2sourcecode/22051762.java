    public static char[] toCharArray(String path) throws IOException {
        final CharArrayWriter writer = new CharArrayWriter();
        readFileAndWriteToWriter(path, writer);
        return writer.toCharArray();
    }
