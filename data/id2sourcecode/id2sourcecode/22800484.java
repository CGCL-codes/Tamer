    public static Configuration buildConfiguration(final String resourceName, final ClassLoader cls) throws ClassNotFoundException {
        try {
            final InputStream is = cls.getResourceAsStream(resourceName);
            if (is == null) {
                return new Configuration();
            }
            final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = db.parse(is);
            final Element components = doc.getDocumentElement();
            return new Configuration(getChannels(components, cls), getEvents(components, cls), getTasks(components, cls));
        } catch (final SAXException e) {
            throw new JeeManagementException(e);
        } catch (final IOException e) {
            throw new JeeManagementException(e);
        } catch (ParserConfigurationException e) {
            throw new JeeManagementException("DocumentBuilder could not be created", e);
        }
    }
