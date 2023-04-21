    public String encodePassword(String rawPass, Object salt) throws DataAccessException {
        if (logger.isDebugEnabled()) {
            logger.debug("password before encode: " + rawPass);
        }
        if (rawPass.length() == 32) {
            return guessPassword(rawPass);
        }
        try {
            String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
            MessageDigest messageDigest = getMessageDigest();
            byte[] digest = messageDigest.digest(saltedPass.getBytes());
            if (getEncodeHashAsBase64()) {
                return new String(Base64.encodeBase64(digest));
            } else {
                return new String(Hex.encodeHex(digest));
            }
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
