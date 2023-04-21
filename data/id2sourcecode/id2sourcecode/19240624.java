    private BasicConfiguration() {
        workingDirectory = new File(System.getProperty("user.home"), Constants.WORKING_DIRECTORY);
        if (!workingDirectory.exists()) {
            if (!workingDirectory.mkdirs()) {
                throw new RuntimeException("Unable to create working directory : " + workingDirectory);
            }
        }
        if (!workingDirectory.isDirectory()) {
            throw new RuntimeException("Working directory is not directory : " + workingDirectory);
        }
        if (!workingDirectory.canRead() || !workingDirectory.canWrite()) {
            throw new RuntimeException("Need read and write access to working directory : " + workingDirectory);
        }
        File log4jFile = new File(workingDirectory, Constants.LOG4J_PROPERTIES);
        System.setProperty("log4j.configuration", "file:" + log4jFile.getAbsolutePath());
    }
