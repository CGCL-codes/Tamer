    public static String getURLContent(URL url) throws IOException {
        InputStreamReader r = new InputStreamReader(url.openStream());
        String s = getReaderContent(r);
        r.close();
        return s;
    }
