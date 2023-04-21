    private InputStream getInputStream(URL url) throws IOException {
        if ("file".equals(url.getProtocol())) {
            String path = url.getFile().replace('/', File.separatorChar);
            path = ParseUtil.decode(path);
            return new FileInputStream(path);
        } else {
            return url.openStream();
        }
    }
