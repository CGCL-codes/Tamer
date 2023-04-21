    public static boolean verifySaltedPassword(byte[] password, String entry) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        if (!entry.startsWith(SSHA)) {
            throw new IllegalArgumentException("Hash not prefixed by {SSHA}; is it really a salted hash?");
        }
        byte[] challenge = Base64.decodeBase64(entry.substring(6).getBytes("UTF-8"));
        byte[] passwordHash = extractPasswordHash(challenge);
        byte[] salt = extractSalt(challenge);
        MessageDigest digest = MessageDigest.getInstance("SHA");
        digest.update(password);
        byte[] hash = digest.digest(salt);
        return Arrays.equals(passwordHash, hash);
    }
