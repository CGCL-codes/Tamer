    protected URLConnection openResource(String name) throws IOException {
        URL url = loader.getResource(name);
        if (url == null) {
            throw new IOException("Can't find resource `" + name + "'");
        }
        URLConnection conn = url.openConnection();
        trace("openResource " + name + " " + conn);
        return conn;
    }
