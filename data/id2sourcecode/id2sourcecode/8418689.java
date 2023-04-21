    public void testShouldBeAbleToAddNewProjectToCurrentConfigXml() throws Exception {
        File configDirectory = FilesystemUtils.createDirectory("cruisecontrol");
        FileUtils.copyFileToDirectory(DataUtils.getConfigXmlAsFile(), configDirectory);
        File config = new File(configDirectory, "config.xml");
        long initLength = config.length();
        service.addProject(config, "CruiseControlOSS", new Svn(null, null));
        String currentFile = FileUtils.readFileToString(config, null);
        assertTrue(StringUtils.contains(currentFile, "CruiseControlOSS"));
        assertTrue(config.length() > initLength);
    }
