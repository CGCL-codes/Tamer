    protected PersistenceUnitInfo getPersistenceUnitInfo() {
        if (this.persistenceUnitInfo == null) {
            this.persistenceUnitInfo = new PersistenceUnitInfo() {

                private List<ClassTransformer> transformers;

                private List<String> managedClasses;

                private List<String> mappingFileNames;

                private ClassLoader classLoader;

                public String getPersistenceUnitName() {
                    return "jomc-standalone";
                }

                public String getPersistenceProviderClassName() {
                    return getPersistenceProvider().getClass().getName();
                }

                public PersistenceUnitTransactionType getTransactionType() {
                    return PersistenceUnitTransactionType.JTA;
                }

                public DataSource getJtaDataSource() {
                    try {
                        return (DataSource) getStandaloneContext().lookup(getStandaloneEnvironment().getJtaDataSourceJndiName());
                    } catch (final NamingException e) {
                        throw new PersistenceException(getMessage(e), e);
                    }
                }

                public DataSource getNonJtaDataSource() {
                    return null;
                }

                public List<String> getMappingFileNames() {
                    try {
                        if (this.mappingFileNames == null) {
                            this.mappingFileNames = this.getPersistenceUnitElements("mapping-file");
                        }
                        return this.mappingFileNames;
                    } catch (final SAXException e) {
                        throw new PersistenceException(getMessage(e), e);
                    } catch (final IOException e) {
                        throw new PersistenceException(getMessage(e), e);
                    } catch (final ParserConfigurationException e) {
                        throw new PersistenceException(getMessage(e), e);
                    }
                }

                public List<URL> getJarFileUrls() {
                    try {
                        final List<URL> jarFileUrls = new LinkedList<URL>();
                        for (final Enumeration<URL> unitUrls = this.getClassLoader().getResources("META-INF/persistence.xml"); unitUrls.hasMoreElements(); ) {
                            final URL unitUrl = unitUrls.nextElement();
                            final String externalForm = unitUrl.toExternalForm();
                            final String jarUrl = externalForm.substring(0, externalForm.indexOf("META-INF"));
                            jarFileUrls.add(new URL(jarUrl));
                        }
                        return jarFileUrls;
                    } catch (final IOException e) {
                        throw new PersistenceException(getMessage(e), e);
                    }
                }

                public URL getPersistenceUnitRootUrl() {
                    return getStandaloneEnvironment().getJpaRootUrl();
                }

                public List<String> getManagedClassNames() {
                    try {
                        if (this.managedClasses == null) {
                            this.managedClasses = this.getPersistenceUnitElements("class");
                        }
                        return this.managedClasses;
                    } catch (final SAXException e) {
                        throw new PersistenceException(getMessage(e), e);
                    } catch (final IOException e) {
                        throw new PersistenceException(getMessage(e), e);
                    } catch (final ParserConfigurationException e) {
                        throw new PersistenceException(getMessage(e), e);
                    }
                }

                public boolean excludeUnlistedClasses() {
                    return true;
                }

                public Properties getProperties() {
                    return getStandaloneEnvironment().getProperties();
                }

                public ClassLoader getClassLoader() {
                    if (this.classLoader == null) {
                        this.classLoader = new URLClassLoader(new URL[] { getStandaloneEnvironment().getJpaRootUrl() }, this.getClass().getClassLoader());
                    }
                    return this.classLoader;
                }

                public void addTransformer(final ClassTransformer transformer) {
                    if (this.transformers == null) {
                        this.transformers = new LinkedList<ClassTransformer>();
                    }
                    this.transformers.add(transformer);
                }

                public ClassLoader getNewTempClassLoader() {
                    final List<URL> jarFileUrls = this.getJarFileUrls();
                    jarFileUrls.add(getStandaloneEnvironment().getJpaRootUrl());
                    return new URLClassLoader(jarFileUrls.toArray(new URL[jarFileUrls.size()]), this.getClass().getClassLoader());
                }

                private List<String> getPersistenceUnitElements(final String name) throws ParserConfigurationException, IOException, SAXException {
                    final List<String> elements = new LinkedList<String>();
                    final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                    factory.setNamespaceAware(true);
                    factory.setValidating(false);
                    final DocumentBuilder documentBuilder = factory.newDocumentBuilder();
                    for (final Enumeration<URL> e = this.getNewTempClassLoader().getResources("META-INF/persistence.xml"); e.hasMoreElements(); ) {
                        final URL url = e.nextElement();
                        final InputStream in = url.openStream();
                        final Document doc = documentBuilder.parse(in);
                        in.close();
                        final NodeList persistenceUnits = doc.getElementsByTagNameNS(PERSISTENCE_NS, "persistence-unit");
                        for (int i = persistenceUnits.getLength() - 1; i >= 0; i--) {
                            final Element persistenceUnit = (Element) persistenceUnits.item(i);
                            final NodeList nodeList = persistenceUnit.getElementsByTagNameNS(PERSISTENCE_NS, name);
                            for (int j = nodeList.getLength() - 1; j >= 0; j--) {
                                final Element element = (Element) nodeList.item(j);
                                elements.add(element.getFirstChild().getNodeValue());
                            }
                        }
                    }
                    return elements;
                }
            };
        }
        return this.persistenceUnitInfo;
    }
