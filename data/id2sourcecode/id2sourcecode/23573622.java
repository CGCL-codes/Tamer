    private byte[] hash(String plaintext) {
        MessageDigest sha = null;
        try {
            sha = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            log.error("Unable to load SHA-1 digest creation code.", e);
        }
        return (sha.digest(plaintext.getBytes()));
    }
