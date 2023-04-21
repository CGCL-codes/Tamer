    public void init() {
        props = new Properties();
        URL url = this.getClass().getResource(RESOURCE);
        if (url == null) {
            DEBUG("Unable to find configuration resource in classpath of name " + RESOURCE + ", using empty configuration.");
            return;
        }
        InputStream is = null;
        try {
            is = url.openStream();
            props.load(is);
        } catch (IOException e) {
            System.err.println("ERROR: Unable to load configuration resource " + RESOURCE + " - " + e.getMessage());
        } finally {
            IOUtil.closeInputStream(is);
        }
    }
