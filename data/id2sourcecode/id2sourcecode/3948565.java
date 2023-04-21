    public ArgsMgr(URL properties_url) throws IOException {
        if (properties_url == null) throw new NullPointerException("PRE-CONDIZIONE VIOLATA!");
        this.properties = new Properties();
        this.properties.load(properties_url.openStream());
    }
