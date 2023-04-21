    public static Properties getAsProperties(String name) {
        Properties props = new Properties();
        URL url = ResourceLoader.getAsUrl(name);
        if (url != null) {
            try {
                props.load(url.openStream());
            } catch (IOException e) {
            }
        }
        return props;
    }
