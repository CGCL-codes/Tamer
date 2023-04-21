    public void ExportKey(byte[] alias, byte[] newKeyStore, byte[] newPassword, byte[] keyPassword) throws Exception {
        FileOutputStream fsKeysOut;
        FileInputStream fsKeysIn = null;
        File fOut;
        Key key = null;
        KeyStore newKey;
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(keyPassword);
        CloseKeyStore();
        try {
            fOut = new File(newKeyStore.toString());
            newKey = KeyStore.getInstance("JCEKS");
            if (fOut.exists()) {
                fsKeysIn = new FileInputStream(fOut);
                newKey.load(fsKeysIn, new String(newPassword).toCharArray());
                fsKeysIn.close();
            }
            key = keyStore.getKey(new String(alias), new String(hash).toCharArray());
            if (newKey.containsAlias(new String(alias))) {
                throw new Exception("key already exits in export target");
            }
            fsKeysOut = new FileOutputStream(new String(newKeyStore));
            newKey.setKeyEntry(new String(alias), key, new String(hash).toCharArray(), null);
            keyStore.store(fsKeysOut, newPassword.toString().toCharArray());
            fsKeysOut.close();
        } catch (Exception e) {
            throw e;
        }
    }
