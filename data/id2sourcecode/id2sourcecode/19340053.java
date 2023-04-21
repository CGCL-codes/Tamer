    public ConfigResource loadConfigResource(ResourceTree resources, NodePath nodeAddress) throws ConfigLoadingException {
        String fileName = nodeAddress + SUFFIX;
        Resource resource = resources.getResource(fileName);
        if (resource == null) return null;
        URL url = resource.getUrl();
        try {
            InputStream inStream = url.openStream();
            if (inStream != null) {
                return new PropertiesConfigResource(resource, inStream, myDefaultEvaluator);
            }
        } catch (IOException e) {
            throw new ConfigLoadingException(url.toString(), null, e);
        }
        return null;
    }
