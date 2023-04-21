    @Test
    public void testCopyFile() throws IOException {
        final File src = new File(srcDir, "ibmrnr.txt");
        final File dst = new File(tempDir, "ibmrnr.txt");
        assertFalse(dst.exists());
        FileUtils.copyFile(src, dst);
        assertTrue(dst.exists());
        assertEquals(TestUtils.readFileToString(src), TestUtils.readFileToString(dst));
    }
