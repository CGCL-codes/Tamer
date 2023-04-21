    public String calculateDigest(byte[] streamBytes, String algoritm) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(algoritm);
        byte[] digest = md.digest(streamBytes);
        String base64codedValue = Base64.encode(digest);
        return base64codedValue;
    }
