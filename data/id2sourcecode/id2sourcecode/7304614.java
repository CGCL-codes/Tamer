        public Cipher getWriterCipher() throws Exception {
            AsymmetricEncryptionConfig aec = node.getConfig().getNetworkConfig().getAsymmetricEncryptionConfig();
            Cipher cipher = Cipher.getInstance(algorithm);
            KeyStore.PrivateKeyEntry pkEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(aec.getKeyAlias(), new KeyStore.PasswordProtection(aec.getKeyPassword().toCharArray()));
            PrivateKey privateKey = pkEntry.getPrivateKey();
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            return cipher;
        }
