    public String createShortDigest(byte[] salt, String identity, int length) {
        sha.reset();
        sha.update(identity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) cat.debug(toHex(pwhash) + " ");
        return new String(Base64.encode(concatenate(pwhash, salt))).substring(0, length);
    }
