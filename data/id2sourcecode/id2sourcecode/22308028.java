    public void testCopyFile() throws Exception {
        String origFileName = "/tmp/utest-cantina-FileUtils";
        String copyFileName = "/tmp/utest-cantina-FileUtils-copy";
        File orig = new File(origFileName);
        File dest = new File(copyFileName);
        Writer opw = new PrintWriter(new FileOutputStream(orig));
        opw.write("coucou!\n");
        opw.close();
        Writer dpw = new PrintWriter(new FileOutputStream(dest));
        dpw.write("yoyoyoyoyoyoyoyoyoyoyoyoyo!\n");
        dpw.close();
        assertFalse(FileUtils.copyFile(origFileName, copyFileName, false));
        assertFalse(orig.length() == dest.length());
        assertTrue(FileUtils.copyFile(origFileName, copyFileName, true));
        File copy = new File(copyFileName);
        assertTrue("File exists", copy.exists());
        assertEquals(copy.length(), orig.length());
    }
