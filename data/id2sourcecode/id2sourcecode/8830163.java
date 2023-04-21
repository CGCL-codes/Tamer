    @TestTargetNew(level = TestLevel.COMPLETE, notes = "Verifies getActions() method.", method = "getActions", args = {  })
    public void test_getActions() {
        assertEquals("getActions should have returned only read", "read", readAllFiles.getActions());
        assertEquals("getActions should have returned all actions", "read,write,execute,delete", allInCurrent.getActions());
    }
