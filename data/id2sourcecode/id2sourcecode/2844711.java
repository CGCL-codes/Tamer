    public void loadDataFloat(URL url) throws IOException {
        if (xBins == 0 || yBins == 0) loadBinSizes(10, 10);
        InputStream is = (url.openStream());
        BufferedInputStream bis = new BufferedInputStream(is);
        loadDataFloat(getBytesFromFile(bis));
    }
