    @Test(expected = AuthenticationException.class)
    public void test10_loginFail() throws AuthenticationException {
        try {
            login.authenticate("f", Digest.digest("XXXXX".getBytes()));
        } catch (NoSuchAlgorithmException e) {
            fail();
        }
    }
