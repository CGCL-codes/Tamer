    public static FortranReader makeReader(String filename) throws IOException {
        FortranReader in;
        URL url;
        try {
            url = new URL(filename);
            InputStreamReader reader = new InputStreamReader(url.openStream());
            in = new FortranReader(reader);
        } catch (MalformedURLException e) {
            in = new FortranReader(new FileReader(filename));
        }
        return in;
    }
