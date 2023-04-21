    private byte[] digestMessage(byte[] msg) throws Exception {
        if (_digest == null) _digest = MessageDigest.getInstance(DIGEST_ALGORITHM);
        return _digest.digest(msg);
    }
