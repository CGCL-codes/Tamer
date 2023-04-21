    private byte[] makeKey(byte[] data) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        byte[] theTextToDigestAsBytes = data;
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(theTextToDigestAsBytes);
        byte[] digest = md.digest();
        return digest;
    }
