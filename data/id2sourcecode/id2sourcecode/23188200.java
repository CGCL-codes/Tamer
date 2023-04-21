                        public Object read(org.orbeon.oxf.pipeline.api.PipelineContext context, ProcessorInput input) {
                            final Locator[] locator = new Locator[1];
                            GrammarReader grammarReader = new XMLSchemaReader(new GrammarReaderController() {

                                public void error(Locator[] locators, String s, Exception e) {
                                    throw new ValidationException(s, e, new LocationData(locators[0]));
                                }

                                public void warning(Locator[] locators, String s) {
                                    throw new ValidationException(s, new LocationData(locators[0]));
                                }

                                public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
                                    URL url = URLFactory.createURL((locator[0] != null && locator[0].getSystemId() != null) ? locator[0].getSystemId() : null, systemId);
                                    InputSource i = new InputSource(url.openStream());
                                    i.setSystemId(url.toString());
                                    return i;
                                }
                            });
                            readInputAsSAX(context, input, new ForwardingContentHandler(grammarReader) {

                                public void setDocumentLocator(Locator loc) {
                                    super.setDocumentLocator(loc);
                                    locator[0] = loc;
                                }
                            });
                            return grammarReader.getResultAsGrammar();
                        }
