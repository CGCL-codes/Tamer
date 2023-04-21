    public String calculateDigest(byte[] streamBytes, String algoritm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algoritm);
        byte[] digest = md.digest(streamBytes);
        BASE64Encoder encoder = new BASE64Encoder();
        String coded = new String(encoder.encodeBuffer(digest));
        return coded.trim();
    }
