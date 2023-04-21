    public static Properties loadProperties(final URL url) throws IOException {
        Properties newprops = new Properties();
        InputStream in = url.openStream();
        newprops.load(in);
        in.close();
        return newprops;
    }
