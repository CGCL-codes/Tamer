    private Properties findProperties(ClassLoader cl) throws IOException {
        URL url = null;
        if (locale != null) {
            url = cl.getResource(resname + "_" + locale.getLanguage() + "_" + locale.getCountry() + "_" + locale.getVariant() + ".properties");
            if (url == null) {
                url = cl.getResource(resname + "_" + locale.getLanguage() + "_" + locale.getCountry() + ".properties");
            }
            if (url == null) {
                url = cl.getResource(resname + "_" + locale.getLanguage() + ".properties");
            }
        }
        if (url == null) {
            url = cl.getResource(resname + ".properties");
        }
        if (url == null) return null;
        Properties pr = new Properties();
        InputStream is = url.openStream();
        pr.load(is);
        is.close();
        return pr;
    }
