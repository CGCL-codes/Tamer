    public static String md5(String source) {
        m.update(source.getBytes(), 0, source.length());
        String md5 = new BigInteger(1, m.digest()).toString(16);
        return ZEROS.substring(0, 32 - md5.length()) + md5;
    }
