    public static String calculate(byte[] byteArray) {
        StringBuffer result = null;
        if (byteArray == null) return null;
        try {
            MessageDigest m = MessageDigest.getInstance("MD5");
            m.update(byteArray);
            result = new StringBuffer(new BigInteger(1, m.digest()).toString(16));
            for (int i = 0; i < 32 - result.length(); i++) result.insert(0, '0');
        } catch (NoSuchAlgorithmException ex) {
            LOG.fatal("MD5 Hashing Failed,System is going down");
            System.exit(1);
        }
        return result.toString();
    }
