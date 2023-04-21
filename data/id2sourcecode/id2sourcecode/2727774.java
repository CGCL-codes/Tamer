    public static String computeMD5(byte[] message) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.reset();
            md5.update(message);
            byte[] digest = md5.digest();
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < digest.length; i++) {
                if ((0xff & digest[i]) < 0x10) sb.append('0');
                sb.append(Integer.toHexString(0xff & digest[i]));
            }
            return (sb.toString());
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
