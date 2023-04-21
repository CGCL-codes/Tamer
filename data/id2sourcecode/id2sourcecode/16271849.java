    public Document parse(URL url) throws CIFException, IOException {
        systemId = url.toString();
        return parse(new BufferedReader(new InputStreamReader(url.openStream())));
    }
