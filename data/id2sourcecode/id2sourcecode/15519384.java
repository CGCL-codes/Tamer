    @Test
    public void test20_login() {
        try {
            LogicFactory factory = login.authenticate("f", Digest.digest("12345".getBytes()));
            userlogic = factory.getUserLogic();
            meetinglogic = factory.getMeetingLogic();
        } catch (Exception e) {
            fail();
        }
    }
