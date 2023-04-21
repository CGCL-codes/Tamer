    private String hashPassword(String pwd, byte[] bSalt) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA-1");
        digest.reset();
        digest.update(bSalt);
        byte[] hash = digest.digest(pwd.getBytes());
        return byteToBase64(hash);
    }
