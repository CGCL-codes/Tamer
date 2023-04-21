    private static BufferedReader newReader(URL url) throws IOException {
        return newReader(url.openConnection().getInputStream());
    }
