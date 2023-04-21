    public static DataFetcher loadData(URL url) throws IOException {
        return loadData(url.openConnection());
    }
