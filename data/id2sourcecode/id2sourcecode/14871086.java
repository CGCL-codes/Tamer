    @Test
    public void test30_createUserFail() {
        try {
            assertFalse(userdb.addUser("f", "", "", Digest.digest("12345".getBytes()), ""));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
