    public TailCall processLeavingTail(XPathContext context) throws XPathException {
        final Controller controller = context.getController();
        final Configuration config = controller.getConfiguration();
        final NamePool namePool = config.getNamePool();
        XPathContext c2 = context.newMinorContext();
        c2.setOrigin(this);
        Result result;
        OutputURIResolver resolver = null;
        if (href == null) {
            result = controller.getPrincipalResult();
        } else {
            try {
                String base;
                if (resolveAgainstStaticBase) {
                    base = baseURI;
                } else {
                    base = controller.getCookedBaseOutputURI();
                }
                resolver = controller.getOutputURIResolver();
                String hrefValue = EscapeURI.iriToUri(href.evaluateAsString(context)).toString();
                try {
                    result = resolver.resolve(hrefValue, base);
                } catch (Exception err) {
                    throw new XPathException("Exception thrown by OutputURIResolver", err);
                }
                if (result == null) {
                    resolver = StandardOutputResolver.getInstance();
                    result = resolver.resolve(hrefValue, base);
                }
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        if (controller.getDocumentPool().find(result.getSystemId()) != null) {
            XPathException err = new XPathException("Cannot write to a URI that has already been read: " + result.getSystemId());
            err.setXPathContext(context);
            err.setErrorCode("XTRE1500");
            throw err;
        }
        if (!controller.checkUniqueOutputDestination(result.getSystemId())) {
            XPathException err = new XPathException("Cannot write more than one result document to the same URI: " + result.getSystemId());
            err.setXPathContext(context);
            err.setErrorCode("XTDE1490");
            throw err;
        } else {
            controller.addUnavailableOutputDestination(result.getSystemId());
            controller.setThereHasBeenAnExplicitResultDocument();
        }
        boolean timing = controller.getConfiguration().isTiming();
        if (timing) {
            String dest = result.getSystemId();
            if (dest == null) {
                if (result instanceof StreamResult) {
                    dest = "anonymous output stream";
                } else if (result instanceof SAXResult) {
                    dest = "SAX2 ContentHandler";
                } else if (result instanceof DOMResult) {
                    dest = "DOM tree";
                } else {
                    dest = result.getClass().getName();
                }
            }
            System.err.println("Writing to " + dest);
        }
        Properties computedGlobalProps = globalProperties;
        if (formatExpression != null) {
            CharSequence format = formatExpression.evaluateAsString(context);
            String[] parts;
            try {
                parts = controller.getConfiguration().getNameChecker().getQNameParts(format);
            } catch (QNameException e) {
                XPathException err = new XPathException("The requested output format " + Err.wrap(format) + " is not a valid QName");
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
            String uri = nsResolver.getURIForPrefix(parts[0], false);
            if (uri == null) {
                XPathException err = new XPathException("The namespace prefix in the format name " + format + " is undeclared");
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
            StructuredQName qName = new StructuredQName(parts[0], uri, parts[1]);
            computedGlobalProps = getExecutable().getOutputProperties(qName);
            if (computedGlobalProps == null) {
                XPathException err = new XPathException("There is no xsl:output format named " + format);
                err.setErrorCode("XTDE1460");
                err.setXPathContext(context);
                throw err;
            }
        }
        Properties computedLocalProps = new Properties(computedGlobalProps);
        final NameChecker checker = config.getNameChecker();
        for (Iterator citer = localProperties.keySet().iterator(); citer.hasNext(); ) {
            String key = (String) citer.next();
            String[] parts = NamePool.parseClarkName(key);
            try {
                setSerializationProperty(computedLocalProps, parts[0], parts[1], localProperties.getProperty(key), nsResolver, true, checker);
            } catch (XPathException e) {
                e.maybeSetLocation(this);
                throw e;
            }
        }
        if (serializationAttributes.size() > 0) {
            for (IntIterator it = serializationAttributes.keyIterator(); it.hasNext(); ) {
                int key = it.next();
                Expression exp = (Expression) serializationAttributes.get(key);
                String value = exp.evaluateAsString(context).toString();
                String lname = namePool.getLocalName(key);
                String uri = namePool.getURI(key);
                try {
                    setSerializationProperty(computedLocalProps, uri, lname, value, nsResolver, false, checker);
                } catch (XPathException e) {
                    e.maybeSetLocation(this);
                    e.maybeSetContext(context);
                    if (NamespaceConstant.SAXON.equals(e.getErrorCodeNamespace()) && "warning".equals(e.getErrorCodeLocalPart())) {
                        try {
                            context.getController().getErrorListener().warning(e);
                        } catch (TransformerException e2) {
                            throw XPathException.makeXPathException(e2);
                        }
                    } else {
                        throw e;
                    }
                }
            }
        }
        if (dynamicOutputElement != null) {
            Item outputArg = dynamicOutputElement.evaluateItem(context);
            if (!(outputArg instanceof NodeInfo && ((NodeInfo) outputArg).getNodeKind() == Type.ELEMENT && ((NodeInfo) outputArg).getFingerprint() == StandardNames.XSL_OUTPUT)) {
                XPathException err = new XPathException("The third argument of saxon:result-document must be an <xsl:output> element");
                err.setLocator(this);
                err.setXPathContext(context);
                throw err;
            }
            Properties dynamicProperties = new Properties();
            Serialize.processXslOutputElement((NodeInfo) outputArg, dynamicProperties, context);
            for (Iterator it = dynamicProperties.keySet().iterator(); it.hasNext(); ) {
                String key = (String) it.next();
                StructuredQName name = StructuredQName.fromClarkName(key);
                String value = dynamicProperties.getProperty(key);
                try {
                    setSerializationProperty(computedLocalProps, name.getNamespaceURI(), name.getLocalName(), value, nsResolver, false, checker);
                } catch (XPathException e) {
                    e.maybeSetLocation(this);
                    e.maybeSetContext(context);
                    throw e;
                }
            }
        }
        String nextInChain = computedLocalProps.getProperty(SaxonOutputKeys.NEXT_IN_CHAIN);
        if (nextInChain != null) {
            try {
                result = controller.prepareNextStylesheet(nextInChain, baseURI, result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        c2.changeOutputDestination(computedLocalProps, result, true, Configuration.XSLT, validationAction, schemaType);
        SequenceReceiver out = c2.getReceiver();
        out.open();
        out.startDocument(0);
        content.process(c2);
        out.endDocument();
        out.close();
        if (resolver != null) {
            try {
                resolver.close(result);
            } catch (TransformerException e) {
                throw XPathException.makeXPathException(e);
            }
        }
        return null;
    }
