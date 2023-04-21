    public void init() throws GfacException {
        try {
            URL url = ClassLoader.getSystemResource(FILENAME);
            this.properties = new Properties();
            this.properties.load(url.openStream());
            String registryURL = loadFromProperty(REGISTY_URL_NAME, true);
            String trustcerts = loadFromProperty(SSL_TRUSTED_CERTS_FILE, true);
            String hostcerts = loadFromProperty(SSL_HOSTCERTS_KEY_FILE, true);
            this.registryService = new XregistryServiceWrapper(registryURL, trustcerts, hostcerts);
        } catch (Exception e) {
            throw new GfacException("Error initialize the generic service", e);
        }
    }
