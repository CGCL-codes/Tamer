    public byte[] sha256Hash(byte[] b) throws OtrCryptoException {
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(b, 0, b.length);
            return sha256.digest();
        } catch (Exception e) {
            throw new OtrCryptoException(e);
        }
    }
