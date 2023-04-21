    private String hashWithMD5(String stringToEncode) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest;
        String encodedString = "";
        digest = md.digest(stringToEncode.getBytes());
        for (int i = 0; i < digest.length; i++) {
            String s = Integer.toHexString(digest[i] & 0xFF);
            encodedString += (s.length() == 1) ? "0" + s : s;
        }
        return encodedString;
    }
