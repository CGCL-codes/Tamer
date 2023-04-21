    private TransactionId(PublicKey pubKey, String digestAlgorithm) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(digestAlgorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        id = HexUtil.toHex(digest.digest(pubKey.getEncoded()));
    }
