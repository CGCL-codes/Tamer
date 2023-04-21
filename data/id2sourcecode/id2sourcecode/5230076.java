    public String createDigest(byte[] salt, String identity) {
        String label = (salt.length > 0) ? "{SSHA}" : "{SHA}";
        sha.reset();
        sha.update(identity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) cat.debug(toHex(pwhash) + " ");
        return label + new String(Base64.encode(concatenate(pwhash, salt)));
    }
