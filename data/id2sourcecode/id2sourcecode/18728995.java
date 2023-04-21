    public SerializationHandler getOutputHandler(Result result) throws TransformerException {
        _method = (String) _properties.get(OutputKeys.METHOD);
        _encoding = (String) _properties.getProperty(OutputKeys.ENCODING);
        _tohFactory = TransletOutputHandlerFactory.newInstance();
        _tohFactory.setEncoding(_encoding);
        if (_method != null) {
            _tohFactory.setOutputMethod(_method);
        }
        if (_indentNumber >= 0) {
            _tohFactory.setIndentNumber(_indentNumber);
        }
        try {
            if (result instanceof SAXResult) {
                final SAXResult target = (SAXResult) result;
                final ContentHandler handler = target.getHandler();
                _tohFactory.setHandler(handler);
                LexicalHandler lexicalHandler = target.getLexicalHandler();
                if (lexicalHandler != null) {
                    _tohFactory.setLexicalHandler(lexicalHandler);
                }
                _tohFactory.setOutputType(TransletOutputHandlerFactory.SAX);
                return _tohFactory.getSerializationHandler();
            } else if (result instanceof DOMResult) {
                _tohFactory.setNode(((DOMResult) result).getNode());
                _tohFactory.setNextSibling(((DOMResult) result).getNextSibling());
                _tohFactory.setOutputType(TransletOutputHandlerFactory.DOM);
                return _tohFactory.getSerializationHandler();
            } else if (result instanceof StreamResult) {
                final StreamResult target = (StreamResult) result;
                _tohFactory.setOutputType(TransletOutputHandlerFactory.STREAM);
                final Writer writer = target.getWriter();
                if (writer != null) {
                    _tohFactory.setWriter(writer);
                    return _tohFactory.getSerializationHandler();
                }
                final OutputStream ostream = target.getOutputStream();
                if (ostream != null) {
                    _tohFactory.setOutputStream(ostream);
                    return _tohFactory.getSerializationHandler();
                }
                String systemId = result.getSystemId();
                if (systemId == null) {
                    ErrorMsg err = new ErrorMsg(ErrorMsg.JAXP_NO_RESULT_ERR);
                    throw new TransformerException(err.toString());
                }
                URL url = null;
                if (systemId.startsWith("file:")) {
                    url = new URL(systemId);
                    _tohFactory.setOutputStream(_ostream = new FileOutputStream(url.getFile()));
                    return _tohFactory.getSerializationHandler();
                } else if (systemId.startsWith("http:")) {
                    url = new URL(systemId);
                    final URLConnection connection = url.openConnection();
                    _tohFactory.setOutputStream(_ostream = connection.getOutputStream());
                    return _tohFactory.getSerializationHandler();
                } else {
                    url = new File(systemId).toURL();
                    _tohFactory.setOutputStream(_ostream = new FileOutputStream(url.getFile()));
                    return _tohFactory.getSerializationHandler();
                }
            }
        } catch (UnknownServiceException e) {
            throw new TransformerException(e);
        } catch (ParserConfigurationException e) {
            throw new TransformerException(e);
        } catch (IOException e) {
            throw new TransformerException(e);
        }
        return null;
    }
