    public TextAdapter(URL url, String map, String params) throws IOException, VisADException {
        DELIM = getDelimiter(url.getFile());
        InputStream is = url.openStream();
        readit(is, map, params);
    }
