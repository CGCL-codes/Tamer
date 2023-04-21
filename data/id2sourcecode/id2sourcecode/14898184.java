    private static synchronized String getText(URL url) throws IOException {
        BufferedInputStream stream = new BufferedInputStream(url.openStream());
        Reader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        CharArrayWriter writer = new CharArrayWriter();
        int ch = reader.read();
        while (ch != -1) {
            writer.write(ch);
            ch = reader.read();
        }
        return writer.toString();
    }
