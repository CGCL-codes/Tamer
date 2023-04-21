    public String calcHash(byte[] data) {
        String hash = null;
        if (digest == null) {
            log.error("Hash cannot be calculated. The MessageDigest has not been initialised probably.");
            hash = "0";
        } else {
            digest.update(data);
            hash = new BigInteger(digest.digest()).toString(16);
        }
        return hash;
    }
