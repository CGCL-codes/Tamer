    public static Properties loadClasspathProperties(String path) throws Exception {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource(path);
        props.load(url.openStream());
        return props;
    }
