    private byte[] digest(byte[] bytes) throws NoSuchAlgorithmException {
        byte[] digestBytes;
        try {
            digestBytes = getMessageDigest().digest(bytes);
        } catch (NoSuchAlgorithmException e) {
            throw e;
        }
        return digestBytes;
    }
