    private URLConnection getURLConnection(URL url) throws IOException {
        String filePath = null;
        boolean isFileProtocol = (url != null && "file".equals(url.getProtocol())) ? true : false;
        if (isFileProtocol) {
            filePath = (url != null) ? url.getPath() : null;
            boolean isRelativePath = (filePath != null && !new File(filePath).isAbsolute()) ? true : false;
            if (isRelativePath) {
                if (log.isDebugEnabled()) {
                    log.debug("WSDL URL has a relative path");
                }
                url = getAbsoluteURL(getThreadClassLoader(), filePath);
                if (url == null) {
                    if (log.isDebugEnabled()) {
                        log.debug("WSDL URL for relative path not found in ClassLoader");
                        log.warn("Unable to read WSDL from relative path, check the relative path");
                        log.info("Relative path example: file:/WEB-INF/wsdl/<wsdlfilename>");
                        log.warn("Using relative path as default wsdl URL to create wsdl Definition.");
                    }
                    url = wsdlURL;
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("WSDL URL found for relative path: " + filePath + " scheme: " + url.getProtocol());
                    }
                }
            }
        }
        URLConnection connection = null;
        if (url != null) {
            if (log.isDebugEnabled()) {
                log.debug("Retrieving URLConnection from WSDL URL");
            }
            connection = openConnection(url);
        }
        return connection;
    }
