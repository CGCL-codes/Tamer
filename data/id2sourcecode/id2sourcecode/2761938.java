    public static String readURL(String URL) throws IOException {
        URL url = new URL(URL);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        return ReaderToString(in);
    }
