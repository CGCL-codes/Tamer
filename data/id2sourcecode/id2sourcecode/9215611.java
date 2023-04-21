    public void loadCapFile(URL url, boolean includeDebug, boolean separateComponents, int blockSize, boolean loadParam, boolean useHash) throws IOException, GPInstallForLoadException, GPLoadException, CardException {
        CapFile cap = null;
        cap = new CapFile(url.openStream(), null);
        loadCapFile(cap, includeDebug, separateComponents, blockSize, loadParam, useHash);
    }
