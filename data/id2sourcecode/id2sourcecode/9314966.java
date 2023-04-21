    private void loadDictionary(URL url) throws Exception {
        InputStream is = new GZIPInputStream(url.openStream());
        readDictionary(url.toString(), is);
    }
