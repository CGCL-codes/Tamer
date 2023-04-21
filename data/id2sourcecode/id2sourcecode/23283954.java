    @Test
    public void copyFile_SameFile() {
        try {
            File fileSrc = Testing.getTempFile(".txt");
            FileUtils.copyFile(fileSrc, fileSrc, false);
            fail("Should have thrown an Exception");
        } catch (IOException ex) {
            assertTrue(true);
        }
    }
