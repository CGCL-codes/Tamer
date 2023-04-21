    public static String md5Converter(String text) {
        String md5val = "";
        MessageDigest algorithm = null;
        try {
            algorithm = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            NetworkLog.logMsg(NetworkLog.LOG_FATAL, Tools.class, "Cannot find digest algorithm");
            System.exit(1);
        }
        byte[] defaultBytes = text.getBytes();
        algorithm.reset();
        algorithm.update(defaultBytes);
        byte messageDigest[] = algorithm.digest();
        StringBuffer hexString = new StringBuffer();
        for (byte element : messageDigest) {
            String hex = Integer.toHexString(0xFF & element);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        md5val = hexString.toString();
        NetworkLog.logMsg(NetworkLog.LOG_INFO, Tools.class, "MD5 of " + text + ": " + md5val);
        return md5val;
    }
