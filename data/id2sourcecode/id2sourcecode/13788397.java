    public final String encryptSHA1(final String cleartext) {
        synchronized (SHA1Digest) {
            SHA1Digest.reset();
            return new BigInteger(SHA1Digest.digest(cleartext.getBytes(UTF8Charset))).toString(16);
        }
    }
