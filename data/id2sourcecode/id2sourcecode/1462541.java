    public void testAddJarToBaseTestsList() throws Exception {
        String runnerOutDir = envController.getRunnerOutDir();
        File jarFile = new File(jarFilePath + "/" + jarFileName);
        jarFile.createNewFile();
        FileUtils.copyFile(jarFile.getAbsolutePath(), runnerOutDir + "/runnerout/testsProject/lib/" + jarFileName);
        jsystem.launch();
        ScenarioUtils.createAndCleanScenario(jsystem, jsystem.getCurrentScenario());
        String txt = jsystem.openJarList();
        report.report(txt);
        File fileToDelete = new File(runnerOutDir + "/runnerout/testsProject/lib/" + jarFileName);
        fileToDelete.delete();
        validateJarList(txt, 4);
    }
