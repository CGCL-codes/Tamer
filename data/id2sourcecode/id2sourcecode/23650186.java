    private InputSource resolveEntityFallback(String publicId, String systemId) throws SAXException, IOException {
        LOG.debug("Resolve failed, fallback scenario");
        if (publicId != null) {
            return null;
        }
        URL url = new URL(systemId);
        if (url.getProtocol().equals("file")) {
            String path = url.getPath();
            File f = new File(path);
            if (!f.canRead()) {
                return resolveEntity(null, f.getName());
            } else {
                return new InputSource(f.getAbsolutePath());
            }
        } else {
            return new InputSource(url.openStream());
        }
    }
