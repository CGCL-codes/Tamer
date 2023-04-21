    private byte[] getDigestValue() {
        digestReset = true;
        return md.digest();
    }
