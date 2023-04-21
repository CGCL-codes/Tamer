    @MediumTest
    public void testCopyFile() throws Exception {
        assertFalse(mCopyFile.exists());
        FileUtils.copyFile(mTestFile, mCopyFile);
        assertTrue(mCopyFile.exists());
        assertEquals(TEST_DATA, FileUtils.readTextFile(mCopyFile, 0, null));
    }
