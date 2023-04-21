    public void load() throws IOException {
        Properties props = new Properties();
        URL url = ClassLoader.getSystemResource("myprops.props");
        props.load(url.openStream());
    }
