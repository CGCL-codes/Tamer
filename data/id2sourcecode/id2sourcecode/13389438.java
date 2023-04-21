    private static Properties loadProps(URL url) throws IOException {
        Properties props = new Properties();
        InputStream in = url.openStream();
        try {
            props.load(in);
        } finally {
            in.close();
        }
        return props;
    }
