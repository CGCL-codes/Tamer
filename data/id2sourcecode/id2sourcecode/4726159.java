    protected static String getMD5(String tohash) {
        byte[] defaultBytes = tohash.getBytes();
        String hashString = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(defaultBytes);
            byte hash[] = md5.digest();
            hashString = Utils.toHexString(hash);
        } catch (NoSuchAlgorithmException nsae) {
        }
        return hashString;
    }
