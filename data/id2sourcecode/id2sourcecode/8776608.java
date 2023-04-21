    static void copyAppEngineXmlFile(String testFolderName, String warFolderName, String fileName) throws IOException {
        String srcPath = warFolderName + (warFolderName.endsWith(File.separator) ? "" : File.separator) + "WEB-INF" + File.separator + fileName;
        File srcFile = new File(srcPath);
        if (srcFile.exists() == false) {
            return;
        }
        File dstFile = new File(testFolderName + (testFolderName.endsWith(File.separator) ? "" : File.separator) + "WEB-INF" + File.separator + fileName);
        FileUtils.copyFile(new File(srcPath), dstFile);
    }
