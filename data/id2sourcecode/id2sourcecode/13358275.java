    public final void changePassword(char[] oldPassword, char[] newPassword) throws Exception {
        if (new String(oldPassword).equals(new String(unwrapStorePassword()))) {
            try {
                KeyGenerator generator = KeyGenerator.getInstance("DES");
                tempKey = generator.generateKey();
                passwdCipher = Cipher.getInstance("DES/CBC/PKCS5Padding");
                passwdCipher.init(Cipher.ENCRYPT_MODE, tempKey);
                rawStore = passwdCipher.doFinal(new String(newPassword).getBytes());
                rawKey = passwdCipher.doFinal(new String(newPassword).getBytes());
                IvParameterSpec dps = new IvParameterSpec(passwdCipher.getIV());
                passwdCipher.init(Cipher.DECRYPT_MODE, tempKey, dps);
                tempKey = null;
            } catch (Exception e) {
                logger.severe("Bad encryption for passwd: ");
                e.printStackTrace();
            }
            Enumeration aliases = keyStore.aliases();
            while (aliases.hasMoreElements()) {
                String alias = (String) aliases.nextElement();
                Key key = keyStore.getKey(alias, oldPassword);
                if (key instanceof PrivateKey) {
                    Certificate[] chain = keyStore.getCertificateChain(alias);
                    keyStore.setKeyEntry(alias, keyStore.getKey(alias, oldPassword), newPassword, chain);
                }
            }
            reWriteKeyStore(new File(keyStoreName));
        } else {
            throw new KeyStoreException("Old password was not correct.");
        }
    }
