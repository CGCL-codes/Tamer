    private static String md5(byte[] string) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(string);
        byte[] digest = md.digest();
        StringBuilder sb = new StringBuilder();
        for (byte b : digest) {
            sb.append(HEX_DIGITS.charAt((b & 0xff) >> 4));
            sb.append(HEX_DIGITS.charAt(b & 0xf));
        }
        return sb.toString();
    }
