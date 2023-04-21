    public static String SHA(String input) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA");
            return byte2hex(sha.digest(input.getBytes()));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
