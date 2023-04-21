    private Properties getProperties(String entry) throws IOException {
        URL url = bContext.getBundle().getEntry(entry);
        Properties props = null;
        if (url != null) {
            props = new Properties();
            props.load(url.openStream());
        }
        return props;
    }
