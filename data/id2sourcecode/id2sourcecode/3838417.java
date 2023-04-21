    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        InputSource result = null;
        if (systemId.startsWith(DTD_PREFIX)) {
            InputStream input = getClass().getResourceAsStream("/database.dtd");
            if (input != null) {
                result = new InputSource(input);
            }
        } else if (systemId.startsWith("file:")) {
            try {
                URL url = new URL(systemId);
                if ("file".equals(url.getProtocol())) {
                    String path = systemId.substring("file:".length());
                    if (path.startsWith("//")) {
                        path = path.substring(2);
                    }
                    result = new InputSource(new FileInputStream(path));
                } else {
                    result = new InputSource(url.openStream());
                }
            } catch (Exception ex) {
                throw new SAXException(ex);
            }
        }
        return result;
    }
