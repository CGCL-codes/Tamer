    public static KeyStore getKeyStore(String location, String type) throws GeneralSecurityException, IOException {
        if (location == null) {
            throw new NullPointerException("location cannot be null");
        }
        KeyStore keystore;
        InputStream in = null;
        try {
            try {
                URL url = new URL(location);
                in = url.openStream();
            } catch (MalformedURLException e) {
                in = new FileInputStream(location);
            }
            in = new BufferedInputStream(in);
            keystore = KeyStore.getInstance(type != null ? type : KeyStore.getDefaultType());
            keystore.load(in, null);
            return keystore;
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                }
            }
        }
    }
