    public javax.microedition.io.Connection open(String name) throws IOException {
        URL url;
        try {
            url = new URL(name);
        } catch (MalformedURLException ex) {
            throw new IOException(ex.toString());
        }
        cn = url.openConnection();
        cn.setDoOutput(true);
        return this;
    }
