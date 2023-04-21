    public static List<ChannelNotification> getChannels(final String resourceName, final ClassLoader cls) throws ClassNotFoundException {
        try {
            final InputStream is = cls.getResourceAsStream(resourceName);
            if (is == null) {
                return Collections.emptyList();
            }
            final DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            final Document doc = db.parse(is);
            final Element channels = doc.getDocumentElement();
            return getChannels(channels, cls);
        } catch (final SAXException e) {
            throw new JeeManagementException(e);
        } catch (final IOException e) {
            throw new JeeManagementException(e);
        } catch (ParserConfigurationException e) {
            throw new JeeManagementException("DocumentBuilder could not be created", e);
        }
    }
