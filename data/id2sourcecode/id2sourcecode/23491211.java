    public KeyStore loadKeyStore(URL url, char[] password) throws GeneralSecurityException, IOException {
        InputStream in = url.openStream();
        try {
            return loadKeyStore(in, password, toKeyStoreType(url.getPath()));
        } finally {
            try {
                in.close();
            } catch (IOException ignore) {
            }
        }
    }
