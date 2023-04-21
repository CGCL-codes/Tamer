    public static KeyStore createKeyStore(OutputStream keystoreWriteStream, String keyStoreType, String keyAlias, char[] password, String userName) throws ConfigurationException, IOException {
        if (null == keyStoreType) {
            keyStoreType = UserConfiguration.defaultKeystoreType();
        }
        if (null == keyAlias) {
            keyAlias = UserConfiguration.defaultKeyAlias();
        }
        keyAlias = keyAlias.toLowerCase();
        if (null == password) {
            password = UserConfiguration.keystorePassword().toCharArray();
        }
        if (null == userName) {
            userName = UserConfiguration.userName();
        }
        KeyStore ks = null;
        try {
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: getting instance of keystore type " + keyStoreType);
            ks = KeyStore.getInstance(keyStoreType);
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: loading key store.");
            ks.load(null, password);
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: key store loaded.");
        } catch (NoSuchAlgorithmException e) {
            generateConfigurationException("Cannot load empty default keystore.", e);
        } catch (CertificateException e) {
            generateConfigurationException("Cannot load empty default keystore with no certificates.", e);
        } catch (KeyStoreException e) {
            generateConfigurationException("Cannot create instance of default key store type.", e);
        } catch (IOException e) {
            generateConfigurationException("Cannot initialize instance of default key store type.", e);
        }
        KeyPairGenerator kpg = null;
        try {
            kpg = KeyPairGenerator.getInstance(UserConfiguration.defaultKeyAlgorithm());
        } catch (NoSuchAlgorithmException e) {
            generateConfigurationException("Cannot generate key using default algorithm: " + UserConfiguration.defaultKeyAlgorithm(), e);
        }
        kpg.initialize(UserConfiguration.defaultKeyLength());
        if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: generating " + UserConfiguration.defaultKeyLength() + "-bit " + UserConfiguration.defaultKeyAlgorithm() + " key.");
        KeyPair userKeyPair = kpg.generateKeyPair();
        if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: key generated, generating certificate for user " + userName);
        String subjectDN = "CN=" + userName;
        X509Certificate ssCert = null;
        try {
            ssCert = MinimalCertificateGenerator.GenerateUserCertificate(userKeyPair, subjectDN, MinimalCertificateGenerator.MSEC_IN_YEAR);
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: certificate generated.");
        } catch (Exception e) {
            generateConfigurationException("InvalidKeyException generating user internal certificate.", e);
        }
        KeyStore.PrivateKeyEntry entry = new KeyStore.PrivateKeyEntry(userKeyPair.getPrivate(), new X509Certificate[] { ssCert });
        try {
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: setting private key entry.");
            ks.setEntry(keyAlias, entry, new KeyStore.PasswordProtection(password));
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: storing key store.");
            ks.store(keystoreWriteStream, password);
            if (Log.isLoggable(Log.FAC_KEYS, Level.FINEST)) Log.finest(Log.FAC_KEYS, "createKeyStore: wrote key store.");
        } catch (NoSuchAlgorithmException e) {
            generateConfigurationException("Cannot save default keystore.", e);
        } catch (CertificateException e) {
            generateConfigurationException("Cannot save default keystore with no certificates.", e);
        } catch (KeyStoreException e) {
            generateConfigurationException("Cannot set private key entry for user default key", e);
        } finally {
            if (keystoreWriteStream != null) {
                try {
                    keystoreWriteStream.close();
                } catch (IOException e) {
                    Log.warning("IOException closing key store file after load.");
                    Log.warningStackTrace(e);
                }
            }
        }
        return ks;
    }
