    public String encrypt(String password) {
        try {
            MessageDigest digest = getMessageDigest();
            digest.reset();
            if ((salt != null) && (salt.trim().length() > 0)) {
                digest.update(salt.getBytes("UTF-8"));
            }
            byte[] encryptedPassword = digest.digest(password.getBytes("UTF-8"));
            for (int a = 0; a < iteration; a++) {
                digest.reset();
                encryptedPassword = digest.digest(encryptedPassword);
            }
            if (storedCredentialsHexEncoded) {
                return new String(Hex.encode(encryptedPassword));
            } else {
                return new String(Base64.encode(encryptedPassword), "UTF-8");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
