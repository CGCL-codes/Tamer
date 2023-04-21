    public Graph loadGraph(URL url) throws IOException {
        return loadGraph(url.openStream());
    }
