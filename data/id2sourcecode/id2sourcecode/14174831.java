    private void completeGlassfishConfiguration() throws IOException {
        final String operatingSystem = System.getProperty("os.name");
        File destFile = null;
        if (operatingSystem != null && operatingSystem.startsWith("Windows")) {
            destFile = new File(containerHome + "/config/asenv.bat");
            final List readLines = FileUtils.readLines(destFile);
            readLines.add("set AS_JAVA=" + System.getProperty("java.home"));
            FileUtils.writeLines(destFile, readLines);
        } else {
            destFile = new File(containerHome + "/config/asenv.conf");
            final List readLines = FileUtils.readLines(destFile);
            readLines.add("AS_JAVA=\"" + System.getProperty("java.home") + "\"");
            FileUtils.writeLines(destFile, readLines);
            Runtime.getRuntime().exec("chmod 754 " + "/tmp/glassfish/bin/asadmin");
        }
    }
