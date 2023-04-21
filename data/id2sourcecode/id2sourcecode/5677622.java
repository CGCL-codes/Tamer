    private void processExportedPackage(Set<File> classPaths, ExportPackage exportPackage, File packageDirectory) throws IOException {
        ExportClassFilter classFilter = new ExportClassFilter(exportPackage);
        String packageName = exportPackage.getName();
        Version packageVersion = exportPackage.getVersion();
        File directory = new File(packageDirectory, packageName + File.separator + packageVersion.toString());
        directory.mkdirs();
        String namePath = packageName.replace('.', File.separatorChar);
        for (File basePath : classPaths) {
            File packagePath = new File(basePath, namePath);
            File[] children = packagePath.listFiles();
            File destPath = new File(directory, namePath);
            destPath.mkdirs();
            if (children != null) {
                for (File child : children) {
                    if (child.isFile() && child.getName().endsWith(CLASS_FILE_EXTENSION)) {
                        if (classFilter.isClassFileExported(child.getName())) {
                            File dest = new File(destPath, child.getName());
                            FileUtils.copyFile(child, dest);
                        }
                    }
                }
            }
        }
    }
