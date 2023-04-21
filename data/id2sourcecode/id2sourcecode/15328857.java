    private void digestTo(MessageDigest md5, byte[] hash) throws GeneralSecurityException {
        md5.digest(hash, 0, hash.length);
    }
