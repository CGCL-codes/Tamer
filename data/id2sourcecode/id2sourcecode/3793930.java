    @Override
    protected char[] digest(String identifier, char[] secret, String algorithm) {
        if (Digest.ALGORITHM_HTTP_DIGEST.equals(algorithm)) {
            String result = DigestUtils.toHttpDigest(identifier, secret, getDigestAuthenticator().getRealm());
            if (result != null) {
                return result.toCharArray();
            }
            return null;
        }
        return super.digest(identifier, secret, algorithm);
    }
