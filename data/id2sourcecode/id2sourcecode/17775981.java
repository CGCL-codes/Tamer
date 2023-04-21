    public ProcessorOutput createOutput(String name) {
        ProcessorOutput output = new ProcessorImpl.CacheableTransformerOutputImpl(getClass(), name) {

            protected void readImpl(org.orbeon.oxf.pipeline.api.PipelineContext context, final ContentHandler contentHandler) {
                try {
                    Document configDoc = readCacheInputAsDOM4J(context, INPUT_CONFIG);
                    final boolean decorateOutput = Boolean.valueOf(XPathUtils.selectStringValueNormalize(configDoc, "/config/decorate")).booleanValue();
                    Schema schema = (Schema) readCacheInputAsObject(context, getInputByName(INPUT_SCHEMA), new CacheableInputReader() {

                        public Object read(org.orbeon.oxf.pipeline.api.PipelineContext context, ProcessorInput input) {
                            try {
                                long time = 0;
                                if (logger.isDebugEnabled()) {
                                    logger.debug("Reading Schema: " + schemaId);
                                    time = System.currentTimeMillis();
                                }
                                Document schemaDoc = readInputAsDOM4J(context, input);
                                LocationData locator = (LocationData) schemaDoc.getRootElement().getData();
                                final String schemaSystemId = (locator != null && locator.getSystemID() != null) ? locator.getSystemID() : null;
                                VerifierFactory verifierFactory = new TheFactoryImpl(getFactory());
                                verifierFactory.setEntityResolver(new EntityResolver() {

                                    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                                        URL url = URLFactory.createURL(schemaSystemId, systemId);
                                        InputSource i = new InputSource(url.openStream());
                                        i.setSystemId(url.toString());
                                        return i;
                                    }
                                });
                                InputSource is = new InputSource(new StringReader(XMLUtils.domToString(schemaDoc)));
                                is.setSystemId(schemaSystemId);
                                synchronized (MSVValidationProcessor.class) {
                                    Schema schema = verifierFactory.compileSchema(is);
                                    if (logger.isDebugEnabled()) logger.debug(schemaId + " : Schema compiled in " + (System.currentTimeMillis() - time));
                                    return schema;
                                }
                            } catch (VerifierConfigurationException vce) {
                                throw new OXFException(vce.getCauseException());
                            } catch (Exception e) {
                                throw new OXFException(e);
                            }
                        }
                    });
                    Verifier verifier = schema.newVerifier();
                    verifier.setErrorHandler(new org.xml.sax.ErrorHandler() {

                        private void generateErrorElement(ValidationException ve) throws SAXException {
                            if (decorateOutput && ve != null) {
                                String systemId = ve.getLocationData().getSystemID();
                                AttributesImpl a = new AttributesImpl();
                                a.addAttribute("", MESSAGE_ATTRIBUTE, MESSAGE_ATTRIBUTE, "CDATA", ve.getSimpleMessage());
                                a.addAttribute("", SYSTEMID_ATTRIBUTE, SYSTEMID_ATTRIBUTE, "CDATA", systemId == null ? "" : systemId);
                                a.addAttribute("", LINE_ATTRIBUTE, LINE_ATTRIBUTE, "CDATA", Integer.toString(ve.getLocationData().getLine()));
                                a.addAttribute("", COLUMN_ATTRIBUTE, COLUMN_ATTRIBUTE, "CDATA", Integer.toString(ve.getLocationData().getCol()));
                                contentHandler.startElement(ORBEON_ERROR_NS, ERROR_ELEMENT, ORBEON_ERROR_PREFIX + ":" + ERROR_ELEMENT, a);
                                contentHandler.endElement(ORBEON_ERROR_NS, ERROR_ELEMENT, ORBEON_ERROR_PREFIX + ":" + ERROR_ELEMENT);
                            } else {
                                throw ve;
                            }
                        }

                        public void error(SAXParseException exception) throws SAXException {
                            generateErrorElement(new ValidationException("Error " + exception.getMessage() + "(schema: " + schemaId + ")", new LocationData(exception)));
                        }

                        public void fatalError(SAXParseException exception) throws SAXException {
                            generateErrorElement(new ValidationException("Fatal Error " + exception.getMessage() + "(schema: " + schemaId + ")", new LocationData(exception)));
                        }

                        public void warning(SAXParseException exception) throws SAXException {
                            generateErrorElement(new ValidationException("Warning " + exception.getMessage() + "(schema: " + schemaId + ")", new LocationData(exception)));
                        }
                    });
                    VerifierHandler verifierHandler = verifier.getVerifierHandler();
                    List dest = Arrays.asList(new Object[] { verifierHandler, contentHandler });
                    long time = 0;
                    if (logger.isDebugEnabled()) {
                        time = System.currentTimeMillis();
                    }
                    readInputAsSAX(context, getInputByName(INPUT_DATA), new TeeContentHandler(dest));
                    if (logger.isDebugEnabled()) {
                        logger.debug(schemaId + " validation completed in " + (System.currentTimeMillis() - time));
                    }
                } catch (Exception e) {
                    throw new OXFException(e);
                }
            }
        };
        addOutput(name, output);
        return output;
    }
