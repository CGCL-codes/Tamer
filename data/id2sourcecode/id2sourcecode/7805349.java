    @TestTargetNew(level = TestLevel.COMPLETE, notes = "", method = "getChannel", args = {  })
    public void test_getChannel() throws Exception {
        assertNull(new ServerSocket().getChannel());
    }
