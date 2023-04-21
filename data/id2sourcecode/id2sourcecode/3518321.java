    public static void copyFiles(File directory, File target) throws IOException {
        File[] allTestFiles = directory.listFiles();
        for (int i = 0; i < allTestFiles.length; i++) {
            if (allTestFiles[i].isDirectory()) {
                String newSubDir = allTestFiles[i].getPath();
                newSubDir = newSubDir.substring(newSubDir.lastIndexOf(File.separator) + 1);
                File targetDir = new File(target, newSubDir);
                targetDir.mkdir();
                copyFiles(allTestFiles[i], targetDir);
            } else {
                FileUtils.copyFile(allTestFiles[i], new File(target, allTestFiles[i].getName()));
            }
        }
    }
