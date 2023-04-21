    protected TransformerHandler getTransformerHandler(OutputStream inOutputStream) throws Exception {
        long time0 = System.currentTimeMillis();
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        if (!transformerFactory.getFeature(javax.xml.transform.sax.SAXResult.FEATURE)) {
            throw new UnsupportedOperationException("Can't use AbstractXSLPage with an XSL Processor that can't output to SAX.");
        }
        if (!transformerFactory.getFeature(javax.xml.transform.sax.SAXSource.FEATURE)) {
            throw new UnsupportedOperationException("Can't use AbstractXSLPage with an XSL Processor that can't take SAX input.");
        }
        SAXTransformerFactory saxTransformerFactory = (SAXTransformerFactory) transformerFactory;
        TransformerHandler transformerHandler = null;
        if (getXslURI() == null) {
            transformerHandler = saxTransformerFactory.newTransformerHandler();
        } else {
            Object o;
            synchronized (templateCache) {
                o = templateCache.get(getXslURI());
            }
            Templates template = null;
            if (o != null) {
                if (Debug.ON) {
                    Debug.assertTrue(o instanceof Templates);
                }
                template = (Templates) o;
            } else {
                String urlPath = getSystemID() + getXslURI();
                URL url = new URL(urlPath);
                if (LOG.isInfoEnabled()) {
                    LOG.info("XSL URL: " + url);
                }
                InputStream stream = url.openStream();
                int fileIndex = urlPath.lastIndexOf('/') + 1;
                String baseURI = "";
                if (fileIndex >= 0) {
                    baseURI = urlPath.substring(0, fileIndex);
                }
                if (LOG.isDebugEnabled()) {
                    LOG.debug("getTransformerHandler: baseURI: " + baseURI + "urlPath: " + urlPath);
                }
                StreamSource source = new StreamSource(stream, baseURI);
                template = saxTransformerFactory.newTemplates(source);
                if (shouldCacheTemplates) {
                    synchronized (templateCache) {
                        templateCache.put(getXslURI(), template);
                    }
                }
            }
            if (LOG.isInfoEnabled()) {
                LOG.info("getTransformerHandler: get compiled XSL: time: " + (System.currentTimeMillis() - time0));
            }
            if (Debug.ON) {
                Debug.assertTrue(template != null, "null template");
            }
            transformerHandler = saxTransformerFactory.newTransformerHandler(template);
        }
        if (Debug.ON) {
            Debug.assertTrue(inOutputStream != null, "null output stream");
        }
        ContentHandler contentHandler;
        if (getXslURI() == null) {
            contentHandler = makeSerializer(inOutputStream, getXMLOutputProperties());
        } else {
            contentHandler = makeSerializer(inOutputStream, getHTMLOutputProperties());
        }
        Result result = new SAXResult(contentHandler);
        transformerHandler.setResult(result);
        return transformerHandler;
    }
