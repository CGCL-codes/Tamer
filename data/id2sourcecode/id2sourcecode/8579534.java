    public AbstractCrypto(Properties properties, ClassLoader loader) throws CredentialException, IOException {
        this.properties = properties;
        String location = this.properties.getProperty("org.apache.ws.security.crypto.merlin.file");
        InputStream is = null;
        java.net.URL url = Loader.getResource(loader, location);
        if (url != null) {
            is = url.openStream();
        } else {
            is = new java.io.FileInputStream(location);
        }
        if (is == null) {
            try {
                is = new FileInputStream(location);
            } catch (Exception e) {
                throw new CredentialException(3, "proxyNotFound", new Object[] { location });
            }
        }
        try {
            String provider = properties.getProperty("org.apache.ws.security.crypto.merlin.keystore.provider");
            String passwd = properties.getProperty("org.apache.ws.security.crypto.merlin.keystore.password", "security");
            String type = properties.getProperty("org.apache.ws.security.crypto.merlin.keystore.type", KeyStore.getDefaultType());
            this.keystore = load(is, passwd, provider, type);
        } finally {
            is.close();
        }
        String cacertsPath = System.getProperty("java.home") + "/lib/security/cacerts";
        InputStream cacertsIs = new FileInputStream(cacertsPath);
        try {
            String cacertsPasswd = properties.getProperty("org.apache.ws.security.crypto.merlin.cacerts.password", "changeit");
            this.cacerts = load(cacertsIs, cacertsPasswd, null, KeyStore.getDefaultType());
        } finally {
            cacertsIs.close();
        }
    }
