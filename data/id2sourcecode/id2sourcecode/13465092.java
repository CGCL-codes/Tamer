    @Test
    public void testMd51() throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte[] aaa = messageDigest.digest("aaa".getBytes());
        System.out.println("aaa:" + new String(aaa, "iso8859_1"));
    }
