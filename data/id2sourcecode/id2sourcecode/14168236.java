    private String getServiceClassName(URL url) throws IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            Properties prop = new Properties();
            prop.load(in);
            return prop.getProperty("org.brandao.brutos.applicationcontext");
        } catch (Exception e) {
            return null;
        } finally {
            if (in != null) in.close();
        }
    }
