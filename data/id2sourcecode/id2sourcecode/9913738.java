    public static Properties createProperties(URL url) {
        Properties p = new Properties();
        try {
            InputStream is = url.openStream();
            p.load(is);
            is.close();
            return p;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
