    public boolean checkDigest(String digest, String identity) {
        if (digest.regionMatches(true, 0, "{SHA}", 0, 5)) {
            digest = digest.substring(5);
        } else if (digest.regionMatches(true, 0, "{SSHA}", 0, 6)) {
            digest = digest.substring(6);
        }
        byte[][] hs = split(Base64.decode(digest), 20);
        byte[] hash = hs[0];
        byte[] salt = hs[1];
        if (verbose) cat.debug(toHex(hash) + " " + toHex(salt));
        sha.reset();
        sha.update(identity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) cat.debug(toHex(pwhash));
        boolean valid = true;
        if (!MessageDigest.isEqual(hash, pwhash)) {
            valid = false;
            cat.warn("doesn't match: " + identity);
        }
        return valid;
    }
