    private String encodePassword(String rawPass, byte[] salt, String encodeAlgorithm) {
        MessageDigest messageDigest = getMessageDigest(encodeAlgorithm);
        messageDigest.update(rawPass.getBytes());
        String encPass;
        if (salt != null) {
            messageDigest.update(salt);
            byte[] hash = combineHashAndSalt(messageDigest.digest(), salt);
            encPass = new String(Base64.encode(hash));
        } else {
            encPass = new String(Base64.encode(messageDigest.digest()));
        }
        return encPass;
    }
