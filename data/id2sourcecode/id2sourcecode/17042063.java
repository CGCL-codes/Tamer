    public static String getDigest(String message) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException nsae) {
            nsae.printStackTrace();
        }
        md.reset();
        byte[] input = message.getBytes();
        md.update(input);
        byte[] output = md.digest();
        md.reset();
        return convertToHexString(output);
    }
