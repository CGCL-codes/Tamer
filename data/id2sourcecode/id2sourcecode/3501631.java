    private static void assertDigestByByte(String expected, MessageDigest md, String data) throws UnsupportedEncodingException {
        byte[] bytes = data.getBytes("UTF-8");
        for (int i = 0; i < bytes.length; ++i) {
            md.update(bytes[i]);
        }
        byte[] digest = md.digest();
        assertEquals(expected, toHexString(digest));
    }
