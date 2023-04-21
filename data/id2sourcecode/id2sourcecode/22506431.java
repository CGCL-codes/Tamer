    public String encryptPassword(String original) {
        if (original.equals("") || !canEncrypt) return (original);
        byte[] enc = null;
        try {
            synchronized (passwordEncryptor) {
                passwordEncryptor.reset();
                String originalSalted = original + passwordSalt;
                enc = passwordEncryptor.digest(originalSalted.getBytes());
                for (int i = 1; i < encryptionRepeat; i++) {
                    enc = passwordEncryptor.digest(enc);
                }
            }
            return (new String(enc, "ISO-8859-1"));
        } catch (Exception e) {
            canEncrypt = false;
            return (original);
        }
    }
