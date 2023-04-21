    public void switchLoggingConfiguration(String newProfile) throws ManagementException {
        try {
            String srcPath = getLog4jPath(newProfile);
            String destPath = getLog4jPath("");
            writeFile(destPath, readFile(srcPath));
        } catch (IOException ioe) {
            throw new ManagementException("Failed to update log4j configuration file for profile '" + newProfile + "'.", ioe);
        }
    }
