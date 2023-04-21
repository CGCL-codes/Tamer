    private void generateWar(String warFileName, String propertiesFileName) throws Exception {
        final File workingDirectory = new File("../.war");
        FileUtils.deleteDirectory(workingDirectory);
        FileUtils.copyDirectory(new File("../web"), workingDirectory);
        FileUtils.copyDirectory(new File("../conf"), new File(workingDirectory, "WEB-INF/classes"));
        if (propertiesFileName != null) {
            File propsFile = new File(propertiesFileName);
            if (propsFile.exists()) {
                FileUtils.copyFile(propsFile, new File(workingDirectory, "WEB-INF/classes/symmetric.properties"));
            }
        }
        JarBuilder builder = new JarBuilder(workingDirectory, new File(warFileName), new File[] { workingDirectory });
        builder.build();
        FileUtils.deleteDirectory(workingDirectory);
    }
