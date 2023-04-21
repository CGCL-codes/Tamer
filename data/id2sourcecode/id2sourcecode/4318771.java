    public static char[] toCharArray(Reader reader) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        copy(reader, writer);
        return writer.toCharArray();
    }
