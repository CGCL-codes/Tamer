    public static String md5(String data) {
        try {
            MessageDigest algo = createMd5();
            byte[] bytes = data.getBytes("utf-8");
            algo.update(bytes);
            return asHex(algo.digest());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
