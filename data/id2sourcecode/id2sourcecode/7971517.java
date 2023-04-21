    @Override
    public byte[] digestKey(byte[] stretchedPw) {
        MessageDigest messageDigest = sha256Digest();
        messageDigest.update(stretchedPw);
        return messageDigest.digest();
    }
