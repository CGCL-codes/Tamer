    private byte[] derive(String secret, String prefix) {
        if (isVoid(secret) || isVoid(prefix)) {
            fatal("Error: derive: secret or prefix is null.");
        }
        try {
            int keyLen = 16;
            String key = prefix + secret;
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(key.getBytes());
            byte rv[] = Arrays.copyOf(digest, keyLen);
            return rv;
        } catch (Exception e) {
            fatal("Error: derive: Unable to derive key: " + e);
        }
        return null;
    }
