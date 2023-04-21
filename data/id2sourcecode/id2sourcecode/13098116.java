    private void commonPartsURLConstructor(URL wsdlURL, ConfigurationContext configContext) throws FileNotFoundException, UnknownHostException, ConnectException, IOException, WSDLException {
        this.configContext = configContext;
        if (log.isDebugEnabled()) {
            log.debug("WSDL4JWrapper(URL,ConfigurationContext) - Looking for wsdl file on client: " + (wsdlURL != null ? wsdlURL.getPath() : null));
        }
        ClassLoader classLoader = (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() {

            public Object run() {
                return Thread.currentThread().getContextClassLoader();
            }
        });
        this.wsdlURL = wsdlURL;
        URLConnection urlCon;
        try {
            urlCon = getPrivilegedURLConnection(this.wsdlURL);
            InputStream is = null;
            try {
                is = getInputStream(urlCon);
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not open url connection. Trying to use " + "classloader to get another URL.");
                }
                String filePath = wsdlURL != null ? wsdlURL.getPath() : null;
                if (filePath != null) {
                    URL url = getAbsoluteURL(classLoader, filePath);
                    if (url == null) {
                        if (log.isDebugEnabled()) {
                            log.debug("Could not locate URL for wsdl. Reporting error");
                        }
                        throw new WSDLException("WSDL4JWrapper : ", e.getMessage(), e);
                    } else {
                        urlCon = openConnection(url);
                        if (log.isDebugEnabled()) {
                            log.debug("Found URL for WSDL from jar");
                        }
                    }
                } else {
                    if (log.isDebugEnabled()) {
                        log.debug("Could not get URL from classloader. Reporting " + "error due to no file path.");
                    }
                    throw new WSDLException("WSDL4JWrapper : ", e.getMessage(), e);
                }
            }
            if (is != null) {
                is.close();
            }
            this.wsdlExplicitURL = urlCon.getURL().toString();
            getDefinition();
        } catch (FileNotFoundException ex) {
            throw ex;
        } catch (UnknownHostException ex) {
            throw ex;
        } catch (ConnectException ex) {
            throw ex;
        } catch (IOException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new WSDLException("WSDL4JWrapper : ", ex.getMessage(), ex);
        }
    }
