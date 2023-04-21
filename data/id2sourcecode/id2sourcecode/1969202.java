    @Test
    public void testCopyFileFileFileBoolean() throws IOException, FileIsADirectoryException {
        final File source = new File(this.testDir.getAbsoluteFile(), "testCopyFileInput.txt");
        final File destination = new File(this.testDir.getAbsoluteFile(), "testCopyFileOutput.tft");
        try {
            this.result = CopyFileUtils.copyFile(source, destination, false);
            assertFalse("", this.result);
        } catch (final Exception fnfe) {
            this.result = fnfe instanceof FileNotFoundException;
            assertTrue("", this.result);
        }
        final String inputString = "Its a beautifull day!!!";
        final String expected = inputString;
        WriteFileUtils.string2File(source, inputString);
        this.result = CopyFileUtils.copyFile(source, destination, false);
        assertTrue("Source file " + source.getName() + " was not copied in the destination file " + destination.getName() + ".", this.result);
        final String compare = ReadFileUtils.readFromFile(destination);
        this.result = expected.equals(compare);
        assertTrue("The content from the source file " + source.getName() + " is not the same as the destination file " + destination.getName() + ".", this.result);
    }
