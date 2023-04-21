    private static void assertDigest(String expected, MessageDigest md, String data) throws UnsupportedEncodingException {
        byte[] bytes = data.getBytes("UTF-8");
        byte[] digest = md.digest(bytes);
        assertEquals(expected, toHexString(digest));
    }
