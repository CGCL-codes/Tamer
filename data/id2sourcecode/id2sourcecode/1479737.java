    private byte[] getDigest(byte[] message) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
        messageDigest.reset();
        messageDigest.update(message);
        return messageDigest.digest();
    }
