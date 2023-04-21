    private void copyClasses(File root) throws Exception {
        File classesFolder = getAndCreateClassesFolder(root);
        File[] subFolders = projectClassesPath.listFiles();
        for (File file : subFolders) {
            if (file.getName().equals("sut")) {
                continue;
            }
            if (file.getName().equals("scenarios")) {
                continue;
            }
            if (file.isDirectory()) {
                FileUtils.copyDirectory(file, new File(classesFolder, file.getName()));
            } else {
                FileUtils.copyFile(file, new File(classesFolder, file.getName()));
            }
        }
    }
