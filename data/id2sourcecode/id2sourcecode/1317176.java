    public Configuration getConfiguration() throws ConfigurationException {
        final ConfigurationContentHandler contentHandler = new ConfigurationContentHandler();
        final XMLReader reader;
        try {
            reader = XMLReaderFactory.createXMLReader();
            reader.setContentHandler(contentHandler);
            reader.setEntityResolver(new SchemaResourceEntityResolver());
            reader.setFeature("http://xml.org/sax/features/validation", true);
            reader.setFeature("http://apache.org/xml/features/validation/schema", true);
        } catch (SAXException e) {
            throw new JTellException("An error occurred while initializing the configuration file parser.", e);
        }
        final List<JTellElement> rootElements = new ArrayList<JTellElement>(m_urls.size());
        for (final URL url : m_urls) {
            try {
                final InputStream inputStream = url.openStream();
                try {
                    reader.parse(new InputSource(inputStream));
                    rootElements.add(contentHandler.getRootElement());
                } finally {
                    inputStream.close();
                }
            } catch (Exception e) {
                if (e instanceof JTellException) {
                    throw (JTellException) e;
                }
                throw new ConfigurationException(String.format("An error occurred while parsing the configuration file at [%s].", url), e);
            }
        }
        final Configuration result = createConfiguration(rootElements);
        if (LOG.isDebugEnabled()) {
            LOG.debug(String.format("Returning [%s] for %d configuration file(s) (%s).", result, m_urls.size(), m_urls));
        }
        return result;
    }
