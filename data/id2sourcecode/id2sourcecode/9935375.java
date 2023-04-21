    public void testAddLink() throws Exception {
        String localDirectory = JSystemProperties.getInstance().getPreference(FrameworkOptions.LOG_FOLDER) + File.separator + "current" + File.separator;
        FileUtils.write("automation.txt", "text");
        File source = new File("automation.txt");
        File destination = new File(localDirectory, "automation.txt");
        FileUtils.copyFile(source, destination);
        report.startLevel("This is add links test!", Reporter.MainFrame);
        report.addLink("add external link under level", source.getAbsolutePath());
        report.addLink("add link with level", "automation.txt");
        ReporterHelper.addLinkToExternalLocation(report, "title2", "automation.txt");
        report.stopLevel();
    }
