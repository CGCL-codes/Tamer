    protected byte[] engineDigest() {
        checkState();
        byte[] digest = digests[0].digest();
        digestReset();
        return digest;
    }
