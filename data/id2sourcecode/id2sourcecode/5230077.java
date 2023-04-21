    public boolean checkShortDigest(String digest, byte[] salt, String identity) {
        int length = digest.length();
        sha.reset();
        sha.update(identity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) cat.debug(toHex(pwhash) + " ");
        String result = new String(Base64.encode(concatenate(pwhash, salt))).substring(0, length);
        return digest.equals(result);
    }
