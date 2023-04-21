    private static void doTest(MessageDigest md4, String expected, String testVector) {
        String result = asHex(md4.digest(testVector.getBytes()));
        assertEquals(expected, result);
    }
