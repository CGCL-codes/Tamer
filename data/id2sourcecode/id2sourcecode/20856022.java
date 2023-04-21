    private String calculateMD5Digest(String txt) {
        MessageDigest md;
        String result;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        byte[] Bytes = md.digest(txt.getBytes());
        BigInteger Big = new BigInteger(1, Bytes);
        result = Big.toString(16);
        while (result.length() < 32) {
            result = "0" + result;
        }
        return result;
    }
