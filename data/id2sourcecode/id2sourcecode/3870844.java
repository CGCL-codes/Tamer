                        public Object read(PipelineContext context, ProcessorInput input) {
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

                                    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
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
