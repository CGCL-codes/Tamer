    public static KeyStore getStore(URL url, String password, String type) throws IOException, GeneralSecurityException {
        if (type == null) {
            throw new IOException("Invalid keystore type; null");
        }
        KeyStore ks = KeyStore.getInstance(type);
        ks.load(url.openStream(), password.toCharArray());
        return ks;
    }
