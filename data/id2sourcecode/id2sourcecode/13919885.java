    private static BufferedReader getPage(URL url) throws IOException {
        return new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
    }
