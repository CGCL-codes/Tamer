    public InputSource resolveEntity(String publicId, String systemId) throws IOException {
        InputStream stream;
        if (systemId.equals(FACES_CONFIG_1_0_DTD_SYSTEM_ID)) {
            stream = ClassUtils.getResourceAsStream(FACES_CONFIG_1_0_DTD_RESOURCE);
        } else if (systemId.equals(FACES_CONFIG_1_1_DTD_SYSTEM_ID)) {
            stream = ClassUtils.getResourceAsStream(FACES_CONFIG_1_1_DTD_RESOURCE);
        } else if (systemId.startsWith("jar:")) {
            URL url = new URL(systemId);
            JarURLConnection conn = (JarURLConnection) url.openConnection();
            conn.setUseCaches(false);
            JarEntry jarEntry = conn.getJarEntry();
            if (jarEntry == null) {
                log.severe("JAR entry '" + systemId + "' not found.");
            }
            stream = conn.getJarFile().getInputStream(jarEntry);
        } else {
            if (_externalContext == null) {
                stream = ClassUtils.getResourceAsStream(systemId);
            } else {
                if (systemId.startsWith("file:")) {
                    systemId = systemId.substring(7);
                }
                stream = _externalContext.getResourceAsStream(systemId);
            }
        }
        if (stream == null) {
            return null;
        }
        InputSource is = new InputSource(stream);
        is.setPublicId(publicId);
        is.setSystemId(systemId);
        is.setEncoding("ISO-8859-1");
        return is;
    }
