    @Override
    public byte[] cipher(byte[] message) throws NoSuchAlgorithmCipherException {
        if (message == null) {
            throw new IllegalArgumentException();
        }
        try {
            return MessageDigest.getInstance("MD5").digest(message);
        } catch (NoSuchAlgorithmException e) {
            throw new NoSuchAlgorithmCipherException(e);
        }
    }
