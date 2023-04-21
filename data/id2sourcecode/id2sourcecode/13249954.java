    public boolean write(Node node, LSOutput destination) throws LSException {
        if (node == null) return false;
        Method getVersion = null;
        XMLSerializer ser = null;
        String ver = null;
        Document fDocument = (node.getNodeType() == Node.DOCUMENT_NODE) ? (Document) node : node.getOwnerDocument();
        try {
            getVersion = fDocument.getClass().getMethod("getXmlVersion", new Class[] {});
            if (getVersion != null) {
                ver = (String) getVersion.invoke(fDocument, (Object[]) null);
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
        String encoding = null;
        if ((encoding = destination.getEncoding()) == null) {
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
        }
        try {
            prepareForSerialization(ser, node);
            ser._format.setEncoding(encoding);
            OutputStream outputStream = destination.getByteStream();
            Writer writer = destination.getCharacterStream();
            String uri = destination.getSystemId();
            if (writer == null) {
                if (outputStream == null) {
                    if (uri == null) {
                        String msg = DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "no-output-specified", null);
                        if (ser.fDOMErrorHandler != null) {
                            DOMErrorImpl error = new DOMErrorImpl();
                            error.fType = "no-output-specified";
                            error.fMessage = msg;
                            error.fSeverity = DOMError.SEVERITY_FATAL_ERROR;
                            ser.fDOMErrorHandler.handleError(error);
                        }
                        throw new LSException(LSException.SERIALIZE_ERR, msg);
                    } else {
                        String expanded = XMLEntityManager.expandSystemId(uri, null, true);
                        URL url = new URL(expanded != null ? expanded : uri);
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
                    }
                } else {
                    ser.setOutputByteStream(outputStream);
                }
            } else {
                ser.setOutputCharStream(writer);
            }
            if (node.getNodeType() == Node.DOCUMENT_NODE) ser.serialize((Document) node); else if (node.getNodeType() == Node.DOCUMENT_FRAGMENT_NODE) ser.serialize((DocumentFragment) node); else if (node.getNodeType() == Node.ELEMENT_NODE) ser.serialize((Element) node); else if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.COMMENT_NODE || node.getNodeType() == Node.ENTITY_REFERENCE_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE || node.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE) {
                ser.serialize(node);
            } else return false;
        } catch (UnsupportedEncodingException ue) {
            if (ser.fDOMErrorHandler != null) {
                DOMErrorImpl error = new DOMErrorImpl();
                error.fException = ue;
                error.fType = "unsupported-encoding";
                error.fMessage = ue.getMessage();
                error.fSeverity = DOMError.SEVERITY_FATAL_ERROR;
                ser.fDOMErrorHandler.handleError(error);
            }
            throw new LSException(LSException.SERIALIZE_ERR, DOMMessageFormatter.formatMessage(DOMMessageFormatter.SERIALIZER_DOMAIN, "unsupported-encoding", null));
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
            e.printStackTrace();
            throw new LSException(LSException.SERIALIZE_ERR, e.toString());
        }
        return true;
    }
