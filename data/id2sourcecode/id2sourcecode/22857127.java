    public static String doMakeMD5(String message) throws NoSuchAlgorithmException, NoSuchProviderException {
        byte[] data = message.getBytes();
        MessageDigest md;
        md = MessageDigest.getInstance("MD5", "Cryptix");
        md.update(data);
        md.update(data);
        byte[] digest1 = md.digest();
        String hashmessage = toHexString(digest1);
        return hashmessage;
    }
