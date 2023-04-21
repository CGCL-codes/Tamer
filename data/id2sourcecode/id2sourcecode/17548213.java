    public byte[] getInfoHash() throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("SHA");
        return digest.digest(infoDictionary.byteArray.toByteArray());
    }
