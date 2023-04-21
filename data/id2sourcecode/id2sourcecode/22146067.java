    public java.io.InputStream getInputStream(String pFileName) throws java.io.IOException {
        try {
            URL url = new URL(pFileName);
            return url.openStream();
        } catch (MalformedURLException mue) {
            return new FileInputStream(new File(pFileName));
        }
    }
