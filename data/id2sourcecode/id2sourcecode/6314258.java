    public OBODoc parse(URL url) throws IOException {
        this.location = url;
        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream(), OBOFormatConstants.DEFAULT_CHARACTER_ENCODING));
        return parse(in);
    }
