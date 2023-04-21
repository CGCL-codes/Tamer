    public void switchLoggingConfiguration(String newProfile) throws IOException {
        try {
            currentProfile = newProfile;
            String srcPath = getLog4jPath(newProfile);
            String destPath = getLog4jPath("");
            writeFile(destPath, readFile(srcPath));
        } catch (IOException ioe) {
            throw new IOException("Failed to update log4j configuration file for profile '" + newProfile + "'.", ioe);
        }
    }
