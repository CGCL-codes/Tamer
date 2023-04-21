    protected KeyStore readKeyStore(InputStream in) throws ConfigurationException {
        KeyStore keyStore = null;
        try {
            if (Log.isLoggable(Log.FAC_KEYS, Level.INFO)) Log.info(Log.FAC_KEYS, "Loading CCN key store...");
            keyStore = KeyStore.getInstance(_keyStoreType);
            keyStore.load(in, _password);
        } catch (NoSuchAlgorithmException e) {
            Log.warning("Cannot load keystore: " + e);
            throw new ConfigurationException("Cannot load default keystore: " + e);
        } catch (CertificateException e) {
            Log.warning("Cannot load keystore with no certificates.");
            throw new ConfigurationException("Cannot load keystore with no certificates.");
        } catch (IOException e) {
            Log.warning("Cannot open existing key store: " + e);
            try {
                in.reset();
                java.io.FileOutputStream bais = new java.io.FileOutputStream("KeyDump.p12");
                try {
                    byte[] tmp = new byte[2048];
                    int read = in.read(tmp);
                    while (read > 0) {
                        bais.write(tmp, 0, read);
                        read = in.read(tmp);
                    }
                    bais.flush();
                } finally {
                    bais.close();
                }
            } catch (IOException e1) {
                Log.info(Log.FAC_KEYS, "Another exception: " + e1);
            }
            throw new ConfigurationException(e);
        } catch (KeyStoreException e) {
            Log.warning("Cannot create instance of preferred key store type: " + _keyStoreType + " " + e.getMessage());
            Log.warningStackTrace(e);
            throw new ConfigurationException("Cannot create instance of default key store type: " + _keyStoreType + " " + e.getMessage());
        } finally {
            if (null != in) try {
                in.close();
            } catch (IOException e) {
                Log.warning("IOException closing key store file after load.");
                Log.warningStackTrace(e);
            }
        }
        return keyStore;
    }
