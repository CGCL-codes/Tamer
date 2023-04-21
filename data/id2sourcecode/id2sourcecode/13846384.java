    private InputSource getInputSource(LSInput input) throws LSException {
        InputSource source = null;
        String systemId = input.getSystemId();
        InputStream in = input.getByteStream();
        if (in != null) {
            source = new InputSource(in);
            source.setSystemId(systemId);
        }
        if (source == null && entityResolver != null) {
            String publicId = input.getPublicId();
            try {
                source = entityResolver.resolveEntity(publicId, systemId);
            } catch (SAXException e) {
                throw new DomLSException(LSException.PARSE_ERR, e);
            } catch (IOException e) {
                throw new DomLSException(LSException.PARSE_ERR, e);
            }
        }
        if (source == null) {
            URL url = null;
            String base = input.getBaseURI();
            try {
                try {
                    URL baseURL = (base == null) ? null : new URL(base);
                    url = (baseURL == null) ? new URL(systemId) : new URL(baseURL, systemId);
                } catch (MalformedURLException e) {
                    File baseFile = (base == null) ? null : new File(base);
                    url = (baseFile == null) ? new File(systemId).toURL() : new File(baseFile, systemId).toURL();
                }
                in = url.openStream();
                systemId = url.toString();
                source = new InputSource(in);
                source.setSystemId(systemId);
            } catch (IOException e) {
                throw new DomLSException(LSException.PARSE_ERR, e);
            }
        }
        return source;
    }
