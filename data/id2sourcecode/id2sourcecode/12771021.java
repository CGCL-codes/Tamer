    String GetEncryptionMethod(byte[] alias, byte[] keyPassword) throws Exception {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] hash = md.digest(keyPassword);
        Key key = null;
        key = keyStore.getKey(new String(alias), new String(hash).toCharArray());
        return key.getAlgorithm();
    }
