    protected void applicationHmeConfig() {
        URL url = null;
        InputStream stream = null;
        try {
            url = context.getServletContext().getResource(APPLICATION_HME_XML);
            if (url != null) {
                stream = url.openStream();
            }
        } catch (MalformedURLException e1) {
        } catch (IOException e) {
        }
        if (stream != null) {
            if (log.isInfoEnabled()) {
                log.info("Adding HME Applications: " + url);
            }
            if (hmeDigester == null) {
                hmeDigester = createHmeDigester(xmlValidation, xmlNamespaceAware);
            }
            synchronized (hmeDigester) {
                try {
                    InputSource is = new InputSource(url.toExternalForm());
                    is.setByteStream(stream);
                    hmeDigester.push(context);
                    ContextErrorHandler errorHandler = new ContextErrorHandler();
                    hmeDigester.setErrorHandler(errorHandler);
                    if (log.isDebugEnabled()) {
                        log.debug("Parsing application web.xml file at " + url.toExternalForm());
                    }
                    hmeDigester.parse(is);
                    if (errorHandler.parseException != null) {
                        ok = false;
                    }
                } catch (SAXParseException e) {
                    log.error(sm.getString("contextConfig.applicationParse", url.toExternalForm()), e);
                    log.error(sm.getString("contextConfig.applicationPosition", "" + e.getLineNumber(), "" + e.getColumnNumber()));
                    ok = false;
                } catch (Exception e) {
                    log.error(sm.getString("contextConfig.applicationParse", url.toExternalForm()), e);
                    ok = false;
                } finally {
                    hmeDigester.reset();
                    try {
                        if (stream != null) {
                            stream.close();
                        }
                    } catch (IOException e) {
                        log.error(sm.getString("contextConfig.applicationClose"), e);
                    }
                }
            }
        }
    }
