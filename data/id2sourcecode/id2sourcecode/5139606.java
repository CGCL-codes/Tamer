    public void loadFileResource(final String resfilepath) throws Exception {
        final URL url = this.getClass().getResource(resfilepath);
        final InputStreamReader ins = new InputStreamReader(url.openStream());
        this.load(ins, String.valueOf(url), String.valueOf(url));
    }
