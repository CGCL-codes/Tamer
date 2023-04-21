    public void load(URL url) throws IOException, NullArgumentException {
        MathUtils.checkNotNull(url);
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
        try {
            DataAdapter da = new StreamDataAdapter(in);
            da.computeStats();
            if (sampleStats.getN() == 0) {
                throw new ZeroException(LocalizedFormats.URL_CONTAINS_NO_DATA, url);
            }
            in = new BufferedReader(new InputStreamReader(url.openStream()));
            fillBinStats(in);
            loaded = true;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
            }
        }
    }
