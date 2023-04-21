    @NotNull
    public Document parse(String systemId, InputSource inputSource, boolean root) throws SAXException, IOException {
        Document dom = documentBuilder.newDocument();
        systemId = normalizeSystemId(systemId);
        core.put(systemId, dom);
        dom.setDocumentURI(systemId);
        if (root) rootDocuments.add(systemId);
        try {
            XMLReader reader = parserFactory.newSAXParser().getXMLReader();
            reader.setContentHandler(getParserHandler(dom));
            if (errorReceiver != null) reader.setErrorHandler(errorReceiver);
            if (options.entityResolver != null) reader.setEntityResolver(options.entityResolver);
            InputStream is = null;
            if (inputSource.getByteStream() != null) {
                is = inputSource.getByteStream();
            }
            if (is == null) {
                int redirects = 0;
                boolean redirect;
                URL url = JAXWSUtils.getFileOrURL(inputSource.getSystemId());
                URLConnection conn = url.openConnection();
                do {
                    redirect = false;
                    try {
                        is = conn.getInputStream();
                    } catch (IOException e) {
                        if (conn instanceof HttpURLConnection) {
                            HttpURLConnection httpConn = ((HttpURLConnection) conn);
                            int code = httpConn.getResponseCode();
                            if (code == 401) {
                                errorReceiver.error(new SAXParseException(WscompileMessages.WSIMPORT_AUTH_INFO_NEEDED(e.getMessage(), systemId, DefaultAuthenticator.defaultAuthfile), null, e));
                                throw new AbortException();
                            }
                        }
                        throw e;
                    }
                    if (conn instanceof HttpURLConnection) {
                        HttpURLConnection httpConn = ((HttpURLConnection) conn);
                        int code = httpConn.getResponseCode();
                        if (code == 302 || code == 303) {
                            List<String> seeOther = httpConn.getHeaderFields().get("Location");
                            if (seeOther != null && seeOther.size() > 0) {
                                URL newurl = new URL(url, seeOther.get(0));
                                if (!newurl.equals(url)) {
                                    errorReceiver.info(new SAXParseException(WscompileMessages.WSIMPORT_HTTP_REDIRECT(code, seeOther.get(0)), null));
                                    url = newurl;
                                    httpConn.disconnect();
                                    if (redirects >= 5) {
                                        errorReceiver.error(new SAXParseException(WscompileMessages.WSIMPORT_MAX_REDIRECT_ATTEMPT(), null));
                                        throw new AbortException();
                                    }
                                    conn = url.openConnection();
                                    redirects++;
                                    redirect = true;
                                }
                            }
                        }
                    }
                } while (redirect);
            }
            inputSource.setByteStream(is);
            reader.parse(inputSource);
            Element doc = dom.getDocumentElement();
            if (doc == null) {
                return null;
            }
            NodeList schemas = doc.getElementsByTagNameNS(SchemaConstants.NS_XSD, "schema");
            for (int i = 0; i < schemas.getLength(); i++) {
                inlinedSchemaElements.add((Element) schemas.item(i));
            }
        } catch (ParserConfigurationException e) {
            errorReceiver.error(e);
            throw new SAXException(e.getMessage());
        }
        return dom;
    }
