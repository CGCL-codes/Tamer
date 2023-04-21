    public void loadDict(URI url) throws IOException {
        InputStream stream = null;
        try {
            stream = url.toURL().openStream();
            if (stream != null) {
                stream = new GZIPInputStream(stream);
            }
            Dict dict = (new CeDictLoader(stream)).load();
            dictList.addDict(dict);
        } finally {
            if (stream != null) {
                stream.close();
            }
        }
    }
