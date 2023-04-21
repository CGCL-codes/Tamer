    public static String md5Digest(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        for (int i = 0; i < s.length(); i++) {
            byte b = (byte) (s.charAt(i) & 0xff);
            digest.update(b);
        }
        byte[] bin = digest.digest();
        char[] result = new char[bin.length * 2];
        int j = 0;
        for (int i = 0; i < bin.length; i++) {
            result[j++] = cnvt[(bin[i] >> 4) & 0xf];
            result[j++] = cnvt[bin[i] & 0xf];
        }
        return new String(result);
    }
