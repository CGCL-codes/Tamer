    public static String MD5hash(String input) {
        MessageDigest diggy;
        try {
            diggy = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
        diggy.update(CharToByte.charsToBytes(input.toCharArray()));
        return CharToByte.bytesToString(diggy.digest());
    }
