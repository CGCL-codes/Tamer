    public static char[] toCharArray(InputStream in, String charset) throws IOException {
        CharArrayWriter writer = new CharArrayWriter();
        InputStreamReader reader = new InputStreamReader(in, charset);
        copy(reader, writer);
        return writer.toCharArray();
    }
