    public Element toXML(URL url) throws Exception {
        return this.toXML(new BufferedReader(new InputStreamReader(url.openStream())));
    }
