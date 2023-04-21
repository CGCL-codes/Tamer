    private byte[] md5(byte[] aBytes) {
        try {
            MessageDigest lMD = MessageDigest.getInstance("MD5");
            return lMD.digest(aBytes);
        } catch (NoSuchAlgorithmException lEx) {
            return null;
        }
    }
