    public byte[] digest(byte[] data) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance((digestAlgorithm != null) ? digestAlgorithm : DEFAULT_DIGEST_ALGORITHM);
        md.update(data);
        return md.digest();
    }
