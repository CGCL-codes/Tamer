    public void testFileCopy() {
        try {
            File file = new File(PATH_TO_TEMPLATE_ARCHIVE_FILE);
            File file1 = new File(PATH_TO_ARCHIVE_FILE_1);
            File file2 = new File(PATH_TO_ARCHIVE_FILE_1);
            assertNotNull("File unavailable for test " + PATH_TO_TEMPLATE_ARCHIVE_FILE, file);
            assertTrue("File unavailable for test " + PATH_TO_TEMPLATE_ARCHIVE_FILE, file.exists());
            FileUtils.copyFile(PATH_TO_TEMPLATE_ARCHIVE_FILE, PATH_TO_ARCHIVE_FILE_1);
            assertEquals("Did not copy file propertly: " + PATH_TO_ARCHIVE_FILE_1, file, file1);
            long startingLength = file1.length();
            FileUtils.copyFile(PATH_TO_ARCHIVE_FILE_1, PATH_TO_ARCHIVE_FILE_1);
            assertEquals("File copied over itself and created an empty file: " + startingLength + " " + file1.length(), startingLength, file1.length());
        } catch (IOException e) {
            fail(e);
        } finally {
            new File(PATH_TO_ARCHIVE_FILE_1).delete();
        }
    }
