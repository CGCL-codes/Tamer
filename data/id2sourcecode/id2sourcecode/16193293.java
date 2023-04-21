    public static String sha1(String data) {
        try {
            MessageDigest algo = createSha1();
            byte[] bytes = data.getBytes("utf-8");
            algo.update(bytes);
            return asHex(algo.digest());
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError(e);
        }
    }
