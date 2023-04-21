    protected char[] digest(String identifier, char[] secret, String algorithm) {
        return DigestUtils.digest(secret, algorithm);
    }
