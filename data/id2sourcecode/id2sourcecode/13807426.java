    public static byte[] getDigest(Payload payload) throws IOException {
        MessageDigest md;
        byte[] digest = {};
        try {
            md = MessageDigest.getInstance(Symbols.DIGEST_ALGORITHM);
            md.update(MessageHandler.getByteFrom(payload));
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return digest;
    }
