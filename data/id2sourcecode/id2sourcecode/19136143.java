    protected void sync(Package pPackage, File pFromDir, File pToDir) throws MojoExecutionException {
        final File fromDir = new File(pFromDir, pPackage.getName());
        final File toDir = new File(pToDir, pPackage.getName());
        if (!toDir.isDirectory() && !toDir.mkdir()) {
            throw new MojoExecutionException("Failed to create directory " + toDir.getPath());
        }
        final DirectoryScanner fromDirScanner = getDirectoryScanner(pPackage, fromDir);
        final DirectoryScanner toDirScanner = getDirectoryScanner(pPackage, toDir);
        final Set<String> toDirFiles = new HashSet<String>();
        for (String file : toDirScanner.getIncludedFiles()) {
            toDirFiles.add(file);
        }
        for (String file : fromDirScanner.getIncludedFiles()) {
            final File source = new File(fromDir, file);
            final File target = new File(toDir, file);
            try {
                FileUtils.copyFile(source, target);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to copy file " + source + " to " + target + ": " + e.getMessage(), e);
            }
            toDirFiles.remove(file);
        }
        for (String file : toDirFiles) {
            final File target = new File(toDir, file);
            try {
                FileUtils.forceDelete(target);
            } catch (IOException e) {
                throw new MojoExecutionException("Unable to delete file " + target + ": " + e.getMessage(), e);
            }
        }
    }
