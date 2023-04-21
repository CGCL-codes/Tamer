    private String getExplicitURI(URL wsdlURL) throws WSDLException {
        if (isDebugEnabled) {
            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + ") ");
        }
        String explicitURI = null;
        ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        try {
            URL url = wsdlURL;
            String filePath = null;
            boolean isFileProtocol = (url != null && "file".equals(url.getProtocol())) ? true : false;
            if (isFileProtocol) {
                filePath = (url != null) ? url.getPath() : null;
                URI uri = null;
                if (url != null) {
                    uri = new URI(url.toString());
                }
                boolean isRelativePath = (filePath != null && !new File(filePath).isAbsolute()) ? true : false;
                if (isRelativePath) {
                    if (isDebugEnabled) {
                        log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): WSDL URL has a relative path");
                    }
                    url = getAbsoluteURL(classLoader, filePath);
                    if (url == null) {
                        if (isDebugEnabled) {
                            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "WSDL URL for relative path not found in ClassLoader");
                            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "Unable to read WSDL from relative path, check the relative path");
                            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "Relative path example: file:/WEB-INF/wsdl/<wsdlfilename>");
                            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "Using relative path as default wsdl URL to load wsdl Definition.");
                        }
                        url = wsdlURL;
                    } else {
                        if (isDebugEnabled) {
                            log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "WSDL URL found for relative path: " + filePath + " scheme: " + uri.getScheme());
                        }
                    }
                }
            }
            URLConnection urlCon = url.openConnection();
            InputStream is = null;
            try {
                is = getInputStream(urlCon);
            } catch (IOException e) {
                if (isDebugEnabled) {
                    log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "Could not open url connection. Trying to use " + "classloader to get another URL.");
                }
                if (filePath != null) {
                    url = getAbsoluteURL(classLoader, filePath);
                    if (url == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Could not locate URL for wsdl. Reporting error");
                        }
                        throw new WSDLException("WSDL4JWrapper : ", e.getMessage(), e);
                    } else {
                        urlCon = url.openConnection();
                        if (log.isDebugEnabled()) {
                            log.debug("Found URL for WSDL from jar");
                        }
                    }
                } else {
                    if (isDebugEnabled) {
                        log.debug(myClassName + ".getExplicitURI(" + wsdlURL + "): " + "Could not get URL from classloader. Reporting " + "error due to no file path.");
                    }
                    throw new WSDLException("WSDLWrapperReloadImpl : ", e.getMessage(), e);
                }
            }
            if (is != null) {
                is.close();
            }
            explicitURI = urlCon.getURL().toString();
        } catch (Exception ex) {
            throw new WSDLException("WSDLWrapperReloadImpl : ", ex.getMessage(), ex);
        }
        return explicitURI;
    }
