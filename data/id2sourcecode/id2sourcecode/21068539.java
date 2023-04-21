    public byte[] getDigestValue() {
        if (this.digest == null) {
            return null;
        }
        return this.digest.digest();
    }
