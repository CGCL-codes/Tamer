    private String calculateDigest(MessageDigest digest, byte[] bytes) {
        String result;
        try {
            digest = (MessageDigest) digest.clone();
            result = new String(Base64.encode(digest.digest(bytes)));
        } catch (CloneNotSupportedException e1) {
            throw new RuntimeException(digest.getAlgorithm() + " doesn't support clone()");
        }
        return result;
    }
