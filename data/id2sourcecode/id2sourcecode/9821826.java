    private static byte[] getDigest(byte[] authenticatedPart) throws NoSuchAlgorithmException {
        final MessageDigest sha256 = MessageDigest.getInstance("SHA256", new BouncyCastleProvider());
        byte[] result = sha256.digest(authenticatedPart);
        return result;
    }
