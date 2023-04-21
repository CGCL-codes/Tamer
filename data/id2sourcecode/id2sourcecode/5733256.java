    @Test
    public void testExecute_UTF16() throws MojoExecutionException, IOException {
        File originalTestLangFolder = new File(projectRoot, TEST_LANG_UTF16_FOLDER);
        if (!inputFolder.isDirectory()) {
            Assert.fail(inputFolder.getAbsolutePath() + " must be a valid directory");
        }
        copyInputFilesForTest(originalTestLangFolder, "UTF-16");
        File verifUTF16ToUnicodeFolder = new File(projectRoot, VERIF_UTF16_TO_UNICODE_FOLDER);
        translationManagerMojo.setCoppermineFolder(new File(projectRoot, TEST_COPPERMINE_FOLDER));
        translationManagerMojo.setInputEncoding("UTF-16");
        translationManagerMojo.setDocFileExtension(".apt");
        translationManagerMojo.execute();
        Assert.assertEquals("Number of generated files (check of the verification folders)", verifUTF16Folder.listFiles(noSvnFilenameFilter).length, verifUTF16ToUnicodeFolder.listFiles(noSvnFilenameFilter).length);
        Assert.assertEquals("Number of generated files (UTF-16)", verifUTF16Folder.listFiles(noSvnFilenameFilter).length, inputFolder.listFiles(noSvnFilenameFilter).length);
        Assert.assertEquals("Number of generated files (unicode)", verifUTF16Folder.listFiles(noSvnFilenameFilter).length, resourceLangFolder.listFiles(noSvnFilenameFilter).length);
        File[] inputFiles = inputFolder.listFiles(noSvnFilenameFilter);
        for (int i = 0; i < inputFiles.length; i += 1) {
            compareFiles(new File(verifUTF16Folder, inputFiles[i].getName()), inputFiles[i]);
            compareFiles(new File(verifUTF16ToUnicodeFolder, inputFiles[i].getName()), new File(translationManagerMojo.getResourceLangFolder(), inputFiles[i].getName()));
        }
        File[] htmlFiles = docFolder.listFiles(noSvnFilenameFilter);
        Assert.assertEquals("Number of html generated files, including the available_translations.html file.", inputFiles.length + 1, htmlFiles.length);
        for (int i = 0; i < htmlFiles.length; i += 1) {
            File fileToCompare = new File(verifUTF16AptFolder, htmlFiles[i].getName());
            compareFiles(fileToCompare, htmlFiles[i]);
        }
    }
