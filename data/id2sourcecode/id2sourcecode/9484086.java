    public void createTemplateSource(ProjectInfo prjInfo) {
        String prjName = FilenameUtils.getBaseName(prjInfo.getFilename());
        String prjPth = FilenameUtils.getFullPath(prjInfo.getFilename());
        sourceFile = new File(prjPth + prjName, prjName + ".asm");
        File template = new File(System.getProperty("user.dir") + "/" + getTemplateFileName());
        try {
            FileUtils.copyFile(template, sourceFile);
        } catch (IOException e) {
            JOptionPane.showMessageDialog((java.awt.Component) picwindow, "Unable to copy temaplate source " + template + " !");
        }
    }
