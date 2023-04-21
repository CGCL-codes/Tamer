    public static String digest(String text) {
        MessageDigest md5 = getInstance();
        byte bytes[] = text.getBytes();
        md5.update(bytes);
        return byteToHexString(md5.digest());
    }
