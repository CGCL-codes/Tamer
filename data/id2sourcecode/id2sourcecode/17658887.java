    @Override
    public String hashApiKey(Long apiKeyUserID, String apiKeyString) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            digest.reset();
            byte[] apiKeyUserIDBytes = String.valueOf(apiKeyUserID).getBytes();
            byte[] salt = Base64.encodeBytesToBytes(apiKeyUserIDBytes);
            digest.update(salt);
            return Base64.encodeBytes(digest.digest(apiKeyString.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            throw new EveManageSecurityException(e);
        }
    }
