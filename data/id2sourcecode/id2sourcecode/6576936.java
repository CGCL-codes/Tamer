    @Transient
    public void setPassword(String password) throws NoSuchAlgorithmException {
        SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        byte[] salt = new byte[8];
        random.nextBytes(salt);
        digest.update(salt);
        byte[] hash = digest.digest(password.getBytes());
        super.setSalt(byteToBase64(salt));
        super.setPassHash(byteToBase64(hash));
    }
