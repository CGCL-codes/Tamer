    public InputSource resolveEntity(String publicId, String systemId) {
        log.debug("Trying to resolve XML entity with public ID [{}] and system ID [{}]", publicId, systemId);
        if (systemId != null && systemId.indexOf(DozerConstants.XSD_NAME) > systemId.lastIndexOf("/")) {
            String fileName = systemId.substring(systemId.indexOf(DozerConstants.XSD_NAME));
            log.debug("Trying to locate [{}] in classpath", fileName);
            try {
                DozerClassLoader classLoader = BeanContainer.getInstance().getClassLoader();
                URL url = classLoader.loadResource(fileName);
                InputStream stream = url.openStream();
                InputSource source = new InputSource(stream);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
                log.debug("Found beanmapping XML Schema [{}] in classpath", systemId);
                return source;
            } catch (Exception ex) {
                log.error("Could not resolve beanmapping XML Schema [" + systemId + "]: not found in classpath", ex);
            }
        }
        return null;
    }
