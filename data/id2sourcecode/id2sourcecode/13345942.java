    public static String encripty(String word) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        BigInteger hash = new BigInteger(1, md.digest(word.getBytes()));
        String s = hash.toString(16);
        if (s.length() % 2 != 0) s = "0" + s;
        return s;
    }
