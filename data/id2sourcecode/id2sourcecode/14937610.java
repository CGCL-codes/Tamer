    public ChordIdentifier(byte[] info) {
        try {
            MessageDigest md = MessageDigest.getInstance(MD_ALGORITHM);
            data = md.digest(info);
        } catch (NoSuchAlgorithmException e) {
            ChordLogger.getInstance().error("Message Digest Algorithm unrecognized: " + e.getMessage());
        }
        bigNumId = new BigInteger(1, data);
    }
