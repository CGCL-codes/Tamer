    public byte[] computeInfoHash() throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        return sha1.digest(infoPacket);
    }
