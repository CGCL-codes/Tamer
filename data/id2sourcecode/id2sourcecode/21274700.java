    public boolean checkDigest(String digest, String entity) {
        boolean valid = true;
        digest = digest.substring(6);
        byte[][] hs = split(org.apache.commons.codec.binary.Base64.decodeBase64(digest.getBytes()), 20);
        byte[] hash = hs[0];
        byte[] salt = hs[1];
        sha.reset();
        sha.update(entity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        log.debug("Salted Hash extracted (in hex): " + toHex(hash) + " " + "nSalt extracted (in hex): " + toHex(salt));
        log.debug("Hash length is: " + hash.length + " Salt length is: " + salt.length);
        log.debug("Salted Hash presented in hex: " + toHex(pwhash));
        if (!MessageDigest.isEqual(hash, pwhash)) {
            valid = false;
            log.debug("Hashes DON'T match: " + entity);
        }
        if (MessageDigest.isEqual(hash, pwhash)) {
            valid = true;
            log.debug("Hashes match: " + entity);
        }
        return valid;
    }
