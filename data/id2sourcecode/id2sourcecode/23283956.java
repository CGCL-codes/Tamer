    @Test
    public void copyFile_NotExists() throws Exception {
        File fileSrc = new File("absolutely_bogus.txt");
        File fileTgt = Testing.getTempFile(".txt");
        try {
            FileUtils.copyFile(fileSrc, fileTgt, false);
            fail("Should have thrown an Exception");
        } catch (Exception ex) {
            assertTrue(true);
        }
    }
