    protected synchronized String generateSessionId() {
        if (this.digest == null) {
            this.digest = getDigest();
        }
        if (this.random == null) {
            this.random = getRandom();
        }
        byte[] bytes = new byte[SESSION_ID_BYTES];
        this.random.nextBytes(bytes);
        bytes = this.digest.digest(bytes);
        return encode(bytes);
    }
