    public URLConnection openConnection(URL url) throws IOException {
        if (getProxy() == null) return url.openConnection();
        return url.openConnection(getProxy());
    }
