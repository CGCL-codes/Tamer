    public static String toPassword(String data) throws NoSuchAlgorithmException {
        byte[] mybytes = data.getBytes();
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] md5digest = md5.digest(mybytes);
        return bytesToHex(md5digest);
    }
