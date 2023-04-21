    private void copyFileToTargetFolder(String outputDir, String filename) {
        File sourceFile = new File(filename);
        File targetDir = new File(outputDir + "/" + IMAGEFOLDER);
        try {
            FileUtils.copyFileToDirectory(sourceFile, targetDir);
        } catch (IOException e) {
            logger.error("Could not copy file to dir", e);
            SwingUtils.showError(Controller.guiMessages.getString("error.copyFile"));
        }
    }
