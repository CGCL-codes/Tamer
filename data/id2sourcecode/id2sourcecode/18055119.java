    protected static String digestElement(Element root, String messageDigestAlgorithm) throws CryptoException {
        String b64EncodedDigest = null;
        byte[] assertionCanonicalBytes;
        try {
            assertionCanonicalBytes = getAssertionCanonicalBytes(root);
        } catch (IOException e) {
            throw new CryptoException(e);
        }
        try {
            b64EncodedDigest = CryptoUtils.digest(assertionCanonicalBytes, messageDigestAlgorithm);
        } catch (CryptoException e) {
            throw new CryptoException(e);
        }
        return b64EncodedDigest;
    }
