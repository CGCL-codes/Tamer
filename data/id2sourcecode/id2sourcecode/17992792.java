    private static BufferedReader getBufferedReader(URL url) throws IOException {
        InputStream stream = url.openStream();
        InputStreamReader reader = new InputStreamReader(stream);
        return new BufferedReader(reader);
    }
