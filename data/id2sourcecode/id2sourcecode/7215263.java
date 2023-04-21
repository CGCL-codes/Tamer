    public void serialize(XMLStreamWriter xmlWriter) throws XMLStreamException {
        if (DEBUG_ENABLED) {
            log.debug("serialize xmlWriter=" + xmlWriter);
        }
        OutputStream os = getOutputStream(xmlWriter);
        if (os != null) {
            if (DEBUG_ENABLED) {
                log.debug("serialize OutputStream optimisation: true");
            }
            String encoding = getCharacterEncoding(xmlWriter);
            OMOutputFormat format = new OMOutputFormat();
            format.setCharSetEncoding(encoding);
            serialize(os, format);
        } else {
            if (DEBUG_ENABLED) {
                log.debug("serialize OutputStream optimisation: false");
            }
            XMLStreamReader xmlReader = getReader();
            reader2writer(xmlReader, xmlWriter);
        }
    }
