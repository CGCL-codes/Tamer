    public void testAddJarToList() throws Exception {
        String runnerOutDir = envController.getRunnerOutDir();
        File jarFile = new File(jarFilePath + File.separatorChar + jarFileName);
        jarFile.createNewFile();
        report.report("DEBUG >>> created the new file " + jarFile.getAbsolutePath());
        FileUtils.copyFile(jarFile.getAbsolutePath(), runnerOutDir + "runnerout" + File.separatorChar + "runner" + File.separatorChar + "lib" + File.separatorChar + jarFileName);
        report.report("DEBUG >>> copied the new jar file to " + runnerOutDir + "runnerout" + File.separatorChar + "runner" + File.separatorChar + "lib" + File.separatorChar + jarFileName);
        jsystem.launch();
        String txt = jsystem.openJarList();
        report.report("DEBUG >>> the jar list is: " + txt + "\n\nlooking for " + jarFileName + " in it!!!");
        validateJarList(txt, 4);
    }
