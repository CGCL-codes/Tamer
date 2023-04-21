    public String MD5Encode(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return byteArrayToHexString(md.digest(password.getBytes()));
    }
