    private static byte[] getDigest(X509Certificate cert, String algorithm) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            return md.digest(cert.getEncoded());
        } catch (GeneralSecurityException e) {
            throw new ProviderException(e);
        }
    }
