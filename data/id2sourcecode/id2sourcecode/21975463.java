    public static Properties loadProperties(String path) {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource(path);
        try {
            props.load(url.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
