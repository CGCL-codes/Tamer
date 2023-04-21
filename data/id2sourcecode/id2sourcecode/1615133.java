    @Test
    public void testMain() throws IOException {
        String copyInputFilePath = "./resources/test_copyInput.gsb";
        File copyInputFile = new File(copyInputFilePath);
        FileUtils.copyFile(new File(JgrisbicatcleanerSuite.inputGrisbiFilePath), copyInputFile);
        String args[] = { copyInputFilePath };
        Main.main(args);
        Grisbi expectedGrisbiObj = new GrisbiFileManager(JgrisbicatcleanerSuite.outputExpectedOnlyNotAssociatedOperationsGrisbiFilePath).getGrisbiFileObj();
        GrisbiFileManager updatedGrisbiManager = new GrisbiFileManager(copyInputFilePath);
        Grisbi updatedGrisbiObj = updatedGrisbiManager.getGrisbiFileObj();
        JgrisbicatcleanerSuite.compareGrisbiObjects(expectedGrisbiObj, updatedGrisbiObj);
        assertEquals(true, FileUtils.compareFiles(new File(JgrisbicatcleanerSuite.inputGrisbiFilePath), new File(updatedGrisbiManager.getBackupGrisbiFilePath())));
    }
