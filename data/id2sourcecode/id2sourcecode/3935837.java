    public static String toString(InputStream in) throws IOException {
        if (in == null) return null;
        Reader reader = new InputStreamReader(in);
        char[] buffer = new char[100];
        CharArrayWriter writer = new CharArrayWriter(1000);
        int read;
        while ((read = reader.read(buffer)) > -1) writer.write(buffer, 0, read);
        return writer.toString();
    }
