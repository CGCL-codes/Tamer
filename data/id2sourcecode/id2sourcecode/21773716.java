    public void testCopyFile() throws IOException, FileIsADirectoryException {
        final File source = new File(this.testDir.getAbsoluteFile(), "testCopyFileInput.txt");
        final File destination = new File(this.testDir.getAbsoluteFile(), "testCopyFileOutput.tft");
        try {
            this.result = CopyFileUtils.copyFile(source, destination);
            assertFalse("", this.result);
        } catch (final Exception fnfe) {
            this.result = fnfe instanceof FileNotFoundException;
            assertTrue("", this.result);
        }
        final String inputString = "Its a beautifull day!!!";
        final String expected = inputString;
        WriteFileUtils.string2File(source, inputString);
        this.result = CopyFileUtils.copyFile(source, destination);
        assertTrue("", this.result);
        final String compare = ReadFileUtils.readFromFile(destination);
        this.result = expected.equals(compare);
        assertTrue("", this.result);
    }
