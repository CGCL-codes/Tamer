    private void copyStylesheet(File targetDirectory) throws IOException {
        if (stylesheetFile != null && stylesheetFile.exists()) {
            File newFile = new File(targetDirectory, stylesheetFile.getName());
            newFile.createNewFile();
            FileUtils.copyFile(stylesheetFile, newFile);
        }
    }
