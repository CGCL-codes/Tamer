    public void loadKeyStore(KeyStoreInfo keyStoreInfo, char[] password, PublicKeyCache publicKeyCache) throws KeyStoreException {
        Enumeration<String> aliases = keyStoreInfo.getKeyStore().aliases();
        String alias;
        KeyStore.PrivateKeyEntry entry = null;
        KeyStore.PasswordProtection passwordProtection = new KeyStore.PasswordProtection(password);
        while (aliases.hasMoreElements()) {
            alias = aliases.nextElement();
            if (keyStoreInfo.getKeyStore().isKeyEntry(alias)) {
                try {
                    entry = (KeyStore.PrivateKeyEntry) keyStoreInfo.getKeyStore().getEntry(alias, passwordProtection);
                } catch (NoSuchAlgorithmException e) {
                    throw new KeyStoreException("Unexpected NoSuchAlgorithm retrieving key for alias : " + alias, e);
                } catch (UnrecoverableEntryException e) {
                    throw new KeyStoreException("Unexpected UnrecoverableEntryException retrieving key for alias : " + alias, e);
                }
                if (null == entry) {
                    Log.warning("Cannot get private key entry for alias: " + alias);
                } else {
                    PrivateKey pk = entry.getPrivateKey();
                    if (null != pk) {
                        X509Certificate certificate = (X509Certificate) entry.getCertificate();
                        if (null != certificate) {
                            PublisherPublicKeyDigest ppkd = new PublisherPublicKeyDigest(certificate.getPublicKey());
                            Log.info("KeyCache: loading signing key {0}, remembering public key in public key cache.", ppkd);
                            addMyPrivateKey(ppkd.digest(), pk);
                            publicKeyCache.remember(certificate, keyStoreInfo.getVersion());
                        } else {
                            Log.warning("Private key for alias: " + alias + " has no certificate entry. No way to get public key. Not caching.");
                        }
                    } else {
                        Log.warning("Cannot retrieve private key for key entry alias " + alias);
                    }
                }
            }
        }
    }
