    public void testSimpleWithLinkToFileWereFileIsInLog() throws Exception {
        FileUtils.write("myFileinRootLog.txt", "file message");
        File f = new File("myFileinRootLog.txt");
        File destination = new File(new File(report.getCurrentTestFolder()).getParent(), f.getName());
        FileUtils.copyFile(f, destination);
        report.addLink("file", f.getName());
    }
