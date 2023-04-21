    protected long hash(MessageDigest digest, String str) {
        try {
            digest.update(str.getBytes(UTF_8));
            return new BigInteger(1, digest.digest()).longValue();
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
