    @Test
    public void testexecute() throws BuildException, IOException {
        final File myFile = new File(tempDir, "testbuild.xml");
        FileUtils.copyFile(new File(srcDir, "testbuild.xml"), myFile);
        final File mydestFile = new File(tempDir, "testbuildaaa.xml");
        final DITAOTCopy ditaotcopy = new DITAOTCopy();
        ditaotcopy.setProject(new Project());
        ditaotcopy.setIncludes(myFile.getPath());
        ditaotcopy.setTodir(tempDir.getPath());
        ditaotcopy.setRelativePaths(mydestFile.getName());
        ditaotcopy.execute();
        assertEquals(TestUtils.readFileToString(myFile), TestUtils.readFileToString(mydestFile));
    }
