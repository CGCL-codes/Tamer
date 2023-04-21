    public final void loadFromConfigXml(URL url) {
        InputStream input = null;
        try {
            InputSource inputSource = new InputSource(url.toExternalForm());
            input = url.openStream();
            inputSource.setByteStream(input);
            XMLReader xr = XMLReaderFactory.createXMLReader();
            Settings.SAXDocumentHandler handler = new SAXDocumentHandler(this);
            xr.setContentHandler(handler);
            xr.setErrorHandler(handler);
            xr.parse(inputSource);
        } catch (Exception ex) {
            if (log.isErrorEnabled()) log.error("ajaxnet4j configuration load fails. Use default configurations.", ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (Exception ex) {
                }
            }
        }
        initJsonConverters();
        jsonConvertersTypes.clear();
        jsonConvertersTypesRemoved.clear();
    }
