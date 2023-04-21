    public boolean writeToURI(Node node, String URI) throws LSException {
        if (node == null) {
            return false;
        }
        Method getXmlVersion = null;
        XMLSerializer ser = null;
        String ver = null;
        String encoding = null;
        Document fDocument = (node.getNodeType() == Node.DOCUMENT_NODE) ? (Document) node : node.getOwnerDocument();
        try {
            getXmlVersion = fDocument.getClass().getMethod("getXmlVersion", new Class[] {});
            if (getXmlVersion != null) {
                ver = (String) getXmlVersion.invoke(fDocument, (Object[]) null);
            }
        } catch (Exception e) {
        }
        if (ver != null && ver.equals("1.1")) {
            if (xml11Serializer == null) {
                xml11Serializer = new XML11Serializer();
                initSerializer(xml11Serializer);
            }
            copySettings(serializer, xml11Serializer);
            ser = xml11Serializer;
        } else {
            ser = serializer;
        }
        try {
            Method getEncoding = fDocument.getClass().getMethod("getInputEncoding", new Class[] {});
            if (getEncoding != null) {
                encoding = (String) getEncoding.invoke(fDocument, (Object[]) null);
            }
        } catch (Exception e) {
        }
        if (encoding == null) {
            try {
                Method getEncoding = fDocument.getClass().getMethod("getXmlEncoding", new Class[] {});
                if (getEncoding != null) {
                    encoding = (String) getEncoding.invoke(fDocument, (Object[]) null);
                }
            } catch (Exception e) {
            }
            if (encoding == null) {
                encoding = "UTF-8";
            }
        }
        try {
            prepareForSerialization(ser, node);
            ser._format.setEncoding(encoding);
            String expanded = XMLEntityManager.expandSystemId(URI, null, true);
            URL url = new URL(expanded != null ? expanded : URI);
            OutputStream out = null;
            String protocol = url.getProtocol();
            String host = url.getHost();
            if (protocol.equals("file") && (host == null || host.length() == 0 || host.equals("localhost"))) {
                out = new FileOutputStream(getPathWithoutEscapes(url.getFile()));
            } else {
                URLConnection urlCon = url.openConnection();
                urlCon.setDoInput(false);
                urlCon.setDoOutput(true);
                urlCon.setUseCaches(false);
                if (urlCon instanceof HttpURLConnection) {
                    HttpURLConnection httpCon = (HttpURLConnection) urlCon;
                    httpCon.setRequestMethod("PUT");
                }
                out = urlCon.getOutputStream();
            }
            ser.setOutputByteStream(out);
            if (node.getNodeType() == Node.DOCUMENT_NODE) ser.serialize((Document) node); else if (node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) ser.serialize((DocumentFragment) node); else if (node.getNodeType() == Node.ELEMENT_NODE) ser.serialize((Element) node); else if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE || node.getNodeType() == Node.ENTITY_REFERENCE_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE || node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                ser.serialize(node);
            } else return false;
        } catch (LSException lse) {
            throw lse;
        } catch (RuntimeException e) {
            if (e == DOMNormalizer.abort) {
                return false;
            }
            throw new LSException(LSException.SERIALIZE_ERR, e.toString());
        } catch (Exception e) {
            if (ser.fDOMErrorHandler != null) {
                DOMErrorImpl error = new DOMErrorImpl();
                error.fException = e;
                error.fMessage = e.getMessage();
                error.fSeverity = DOMError.SEVERITY_ERROR;
                ser.fDOMErrorHandler.handleError(error);
            }
            throw new LSException(LSException.SERIALIZE_ERR, e.toString());
        }
        return true;
    }
