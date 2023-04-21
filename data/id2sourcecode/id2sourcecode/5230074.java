    public String createHL7Digest(byte[] salt, String identity, int length) {
        sha.reset();
        sha.update(identity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) cat.debug(toHex(pwhash) + " ");
        StringBuffer result = new StringBuffer(new String(Base64.encode(concatenate(pwhash, salt))));
        result.setLength(length);
        for (int i = 0; i < length; i++) {
            if (result.charAt(i) == '^') result.setCharAt(i, 'A');
        }
        return result.toString();
    }
