    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        LOGGER.fine(String.format("Resolve entity PublicID=%s SystemId=%s", publicId, systemId));
        InputStreamReader reader = null;
        try {
            InputStream input;
            if (uri2url.containsKey(publicId)) {
                LOGGER.info("Entity is a known publicId: " + publicId);
                systemId = uri2url.get(publicId);
                input = getClass().getResourceAsStream(uri2url.get(publicId));
            } else if (systemId.matches("\\w+:.+")) {
                LOGGER.info("Entity is a URI: " + systemId);
                URI uri = new URI(systemId);
                input = uri.toURL().openStream();
            } else {
                LOGGER.fine("Unknown schema systemId: " + systemId);
                if (systemId.startsWith("/")) {
                    LOGGER.info("Entity is an absolute path: " + systemId);
                    File file = new File(systemId);
                    if (file.exists()) input = new FileInputStream(file); else input = getClass().getResourceAsStream(systemId);
                } else {
                    systemId = fallbackSearchLoc.getPath() + systemId;
                    URL locurl = new URL(fallbackSearchLoc.getProtocol(), fallbackSearchLoc.getHost(), fallbackSearchLoc.getPort(), systemId);
                    LOGGER.info("Entity is a relative path: " + locurl.toString());
                    input = locurl.openStream();
                }
                if (input == null) throw new FileNotFoundException();
            }
            if (!input.markSupported()) input = new java.io.BufferedInputStream(input);
            String enc = TBXResolver.getEncoding(input);
            reader = new InputStreamReader(input, enc);
        } catch (FileNotFoundException err) {
            String msg = String.format("Entity could not be resolved:\n  PUBLIC: '%s'\n  SYSTEM: '%s'\n  BUILT IN: %s", publicId, systemId, uri2url.containsKey(publicId) ? uri2url.get(publicId) : "NONE");
            throw new FileNotFoundException(msg);
        } catch (UnsupportedEncodingException err) {
            throw new UnsupportedEncodingException(String.format("PUBLIC %s SYSTEM %s", publicId, systemId));
        } catch (URISyntaxException err) {
            throw new SAXException("Invalid System ID format", err);
        }
        InputSource ret = new InputSource(reader);
        ret.setPublicId(publicId);
        ret.setSystemId(systemId);
        return ret;
    }
