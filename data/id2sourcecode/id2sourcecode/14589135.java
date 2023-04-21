    public byte[] buildBits(byte[] contentbytes) throws NoSuchAlgorithmException {
        MessageDigest hash = MessageDigest.getInstance("SHA1");
        hash.update(contentbytes);
        byte[] digest = hash.digest();
        String hhStr = "30213009" + "06052b0e03021a" + "0500" + "0414" + ApduData.toHexString(digest);
        return ApduData.parse(hhStr);
    }
