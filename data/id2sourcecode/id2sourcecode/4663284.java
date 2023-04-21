    public Configuration buildConfiguration(final String resourceName, final ClassLoader cls) throws ClassNotFoundException {
        final InputStream is = cls.getResourceAsStream(resourceName);
        if (is == null) {
            if (logger.isInfoEnabled()) {
                logger.info("No found resource file for name " + resourceName);
            }
            return new Configuration();
        }
        try {
            final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = db.parse(is);
            final Element components = doc.getDocumentElement();
            return new Configuration(getChannels(components, cls), getEvents(components, cls), getTasks(components, cls), getScriptMonitorManager(components, cls));
        } catch (final SAXException e) {
            throw new AdapterManagementException(e);
        } catch (final IOException e) {
            throw new AdapterManagementException(e);
        } catch (final ParserConfigurationException e) {
            throw new AdapterManagementException("DocumentBuilder could not be created", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (final IOException e) {
                    if (logger.isDebugEnabled()) {
                        logger.debug("I/O problem closing resource " + resourceName, e);
                    }
                }
            }
        }
    }
