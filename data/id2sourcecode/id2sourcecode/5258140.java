    public static String digestMD5(String data) {
        String MD5hex = "";
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(data.getBytes());
            MD5hex = new BigInteger(1, digest).toString(16);
            while (MD5hex.length() < 32) MD5hex = "0" + MD5hex;
        } catch (NoSuchAlgorithmException e) {
            if (log.isErrorEnabled()) log.error("NoSuchAlgorithmException while creating MD5 digest", e);
        }
        return MD5hex;
    }
