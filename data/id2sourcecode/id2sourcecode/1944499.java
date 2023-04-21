                public List<String> getManagedClassNames() {
                    try {
                        if (this.managedClasses == null) {
                            this.managedClasses = new LinkedList<String>();
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
                                    final NodeList classes = persistenceUnit.getElementsByTagNameNS(PERSISTENCE_NS, "class");
                                    for (int j = classes.getLength() - 1; j >= 0; j--) {
                                        final Element clazz = (Element) classes.item(j);
                                        this.managedClasses.add(clazz.getFirstChild().getNodeValue());
                                    }
                                }
                            }
                        }
                        return this.managedClasses;
                    } catch (final SAXException e) {
                        getLogger().fatal(e);
                        throw new RuntimeException(e);
                    } catch (final IOException e) {
                        getLogger().fatal(e);
                        throw new RuntimeException(e);
                    } catch (final ParserConfigurationException e) {
                        getLogger().fatal(e);
                        throw new RuntimeException(e);
                    }
                }
