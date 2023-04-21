    public String createDigest(byte[] salt, String entity) {
        String label = "{SSHA}";
        sha.reset();
        sha.update(entity.getBytes());
        sha.update(salt);
        byte[] pwhash = sha.digest();
        if (verbose) {
            log.debug("pwhash, binary represented as hex: " + toHex(pwhash) + " n");
            log.debug("Putting it all together: ");
            log.debug("binary digest of password plus binary salt: " + Arrays.toString(pwhash) + Arrays.toString(salt));
            log.debug("Now we base64 encode what is respresented above this line ...");
        }
        return label + new String(org.apache.commons.codec.binary.Base64.encodeBase64(concatenate(pwhash, salt)));
    }
