    private int existsDir(String path) throws IOException {
        WebdavResource[] files = webdavResource.listWebdavResources();
        if (files != null) {
            for (WebdavResource file : files) {
                String name = encode(file.getName());
                if (name.equals(path)) {
                    log.info(path + " exists on WebDAV " + httpURL.toString() + " as " + (file.isCollection() ? " directory" : " file"));
                    return (file.isCollection() ? -1 : 1);
                } else {
                }
            }
        } else if (webdavResource.getStatusCode() == HttpStatus.SC_METHOD_NOT_ALLOWED) {
            URL url = new URL(httpURL.toString() + path + "?exists");
            log.debug("WebDAV check failed from " + httpURL.toString() + ", " + webdavResource.getStatusMessage() + ", using HTTP Servlet instead");
            URLConnection connection = url.openConnection();
            connection.setUseCaches(false);
            InputStream in = null;
            try {
                in = connection.getInputStream();
                if (in.read() == 1) {
                    log.info(path + " exists on HTTP " + httpURL.toString());
                    return 1;
                }
            } finally {
                if (in != null) in.close();
            }
        } else {
            throw new IOException("Exists check of " + path + " from WebDAV " + httpURL.toString() + " failed: " + webdavResource.getStatusMessage());
        }
        return 0;
    }
