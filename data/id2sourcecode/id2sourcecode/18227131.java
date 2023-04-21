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
