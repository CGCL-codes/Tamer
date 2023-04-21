    public boolean writeToURI(Node nodeArg, String uri) throws LSException {
        if (nodeArg == null) {
            return false;
        }
        Serializer serializer = fXMLSerializer;
        serializer.reset();
        if (nodeArg != fVisitedNode) {
            String xmlVersion = getXMLVersion(nodeArg);
            fEncoding = getInputEncoding(nodeArg);
            if (fEncoding == null) {
                fEncoding = fEncoding != null ? fEncoding : getXMLEncoding(nodeArg) == null ? "UTF-8" : getXMLEncoding(nodeArg);
            }
            serializer.getOutputFormat().setProperty("version", xmlVersion);
            fDOMConfigProperties.setProperty(DOMConstants.S_XERCES_PROPERTIES_NS + DOMConstants.S_XML_VERSION, xmlVersion);
            fDOMConfigProperties.setProperty(DOMConstants.S_XSL_OUTPUT_ENCODING, fEncoding);
            if ((nodeArg.getNodeType() != Node.DOCUMENT_NODE || nodeArg.getNodeType() != Node.ELEMENT_NODE || nodeArg.getNodeType() != Node.ENTITY_NODE) && ((fFeatures & XMLDECL) != 0)) {
                fDOMConfigProperties.setProperty(DOMConstants.S_XSL_OUTPUT_OMIT_XML_DECL, DOMConstants.DOM3_DEFAULT_FALSE);
            }
            fVisitedNode = nodeArg;
        }
        fXMLSerializer.setOutputFormat(fDOMConfigProperties);
        try {
            if (uri == null) {
                String msg = Utils.messages.createMessage(MsgKey.ER_NO_OUTPUT_SPECIFIED, null);
                if (fDOMErrorHandler != null) {
                    fDOMErrorHandler.handleError(new DOMErrorImpl(DOMError.SEVERITY_FATAL_ERROR, msg, MsgKey.ER_NO_OUTPUT_SPECIFIED));
                }
                throw new LSException(LSException.SERIALIZE_ERR, msg);
            } else {
                String absoluteURI = SystemIDResolver.getAbsoluteURI(uri);
                URL url = new URL(absoluteURI);
                OutputStream urlOutStream = null;
                String protocol = url.getProtocol();
                String host = url.getHost();
                if (protocol.equalsIgnoreCase("file") && (host == null || host.length() == 0 || host.equals("localhost"))) {
                    urlOutStream = new FileOutputStream(getPathWithoutEscapes(url.getPath()));
                } else {
                    URLConnection urlCon = url.openConnection();
                    urlCon.setDoInput(false);
                    urlCon.setDoOutput(true);
                    urlCon.setUseCaches(false);
                    urlCon.setAllowUserInteraction(false);
                    if (urlCon instanceof HttpURLConnection) {
                        HttpURLConnection httpCon = (HttpURLConnection) urlCon;
                        httpCon.setRequestMethod("PUT");
                    }
                    urlOutStream = urlCon.getOutputStream();
                }
                serializer.setOutputStream(urlOutStream);
            }
            if (fDOMSerializer == null) {
                fDOMSerializer = (DOM3Serializer) serializer.asDOM3Serializer();
            }
            if (fDOMErrorHandler != null) {
                fDOMSerializer.setErrorHandler(fDOMErrorHandler);
            }
            if (fSerializerFilter != null) {
                fDOMSerializer.setNodeFilter(fSerializerFilter);
            }
            fDOMSerializer.setNewLine(fEndOfLine.toCharArray());
            fDOMSerializer.serializeDOM3(nodeArg);
        } catch (LSException lse) {
            throw lse;
        } catch (RuntimeException e) {
            throw (LSException) createLSException(LSException.SERIALIZE_ERR, e).fillInStackTrace();
        } catch (Exception e) {
            if (fDOMErrorHandler != null) {
                fDOMErrorHandler.handleError(new DOMErrorImpl(DOMError.SEVERITY_FATAL_ERROR, e.getMessage(), null, e));
            }
            throw (LSException) createLSException(LSException.SERIALIZE_ERR, e).fillInStackTrace();
        }
        return true;
    }
