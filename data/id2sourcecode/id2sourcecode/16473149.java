    public byte[] createHash(String algorithm, byte[] data) throws SeppSecurityException {
        try {
            MessageDigest digest = MessageDigest.getInstance(algorithm, "IAIK");
            return digest.digest(data);
        } catch (Exception e) {
            throw new SeppSecurityException("Couldn't create hash value provided data. Reason: " + e.getMessage());
        }
    }
