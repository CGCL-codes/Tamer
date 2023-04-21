    public static String md5sum(String input) {
        byte[] defaultBytes = input.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                hexString.append(Integer.toHexString(0xF0 & messageDigest[i]).charAt(0));
                hexString.append(Integer.toHexString(0x0F & messageDigest[i]).charAt(0));
            }
        } catch (NoSuchAlgorithmException nsae) {
            logger.info("md5sum: " + nsae.getMessage());
        }
        return hexString.toString();
    }
