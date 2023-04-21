    public byte[] hash(byte[] value) {
        try {
            MessageDigest digester = MessageDigest.getInstance(digestAlgorithm);
            digester.update(salt);
            digester.update(value);
            return digester.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
