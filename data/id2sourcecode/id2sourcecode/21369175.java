    public static final String makePasswordHash(String password) {
        return new BigInteger(1, md5Digester.digest(password.getBytes())).toString(16);
    }
