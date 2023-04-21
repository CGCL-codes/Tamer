    public String crypt(String key) {
        try {
            byte[] hash = MessageDigest.getInstance("MD5").digest(key.getBytes());
            return toHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new Error("Erreur");
        }
    }
