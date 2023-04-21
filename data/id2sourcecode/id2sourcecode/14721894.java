    private SphinxProperties(String contextName, URL url) throws IOException {
        this.contextName = contextName;
        this.url = url;
        props = new Properties();
        if (url != null) {
            props.load(url.openStream());
        }
    }
