    public java.io.InputStream getInputStream(java.net.URL url) throws java.io.IOException {
        java.net.URL url2 = formatURL(url);
        return url2.openStream();
    }
