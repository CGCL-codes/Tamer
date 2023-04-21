    private void extractClasses(File zipFileDirectory) throws Exception {
        File zipDirectoryClassesDir = new File(zipFileDirectory, projectClassesPath.getName());
        if (!zipDirectoryClassesDir.exists()) {
            log.fine("classes dir was not found in extracted project file. " + zipDirectoryClassesDir.getAbsolutePath());
            return;
        }
        File[] subFolders = projectClassesPath.listFiles();
        for (File file : subFolders) {
            if (file.getName().equals("sut")) {
                continue;
            }
            if (file.getName().equals("scenarios")) {
                continue;
            }
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                file.delete();
            }
        }
        subFolders = zipDirectoryClassesDir.listFiles();
        for (File file : subFolders) {
            if (file.getName().equals("sut")) {
                continue;
            }
            if (file.getName().equals("scenarios")) {
                continue;
            }
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, new File(projectClassesPath, file.getName()));
            } else {
                FileUtils.copyFile(file, new File(projectClassesPath, file.getName()));
            }
        }
    }
