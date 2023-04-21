    static InputStream getInputStream(URL url) throws Exception {
        if (c_ResourceLoader != null) {
            return c_ResourceLoader.getZipResource(url).getInputStream();
        } else {
            return url.openStream();
        }
    }
