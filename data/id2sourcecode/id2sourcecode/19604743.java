    private Properties loadProps(URL url) throws IOException {
        Properties prop = new Properties();
        if (url != null) {
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            prop.load(in);
        }
        return prop;
    }
