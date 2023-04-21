    private static String getHash(String string) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            String result = "";
            for (Byte b : md.digest(string.getBytes())) {
                result += Integer.toHexString(0xff & b);
            }
            return result;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }
