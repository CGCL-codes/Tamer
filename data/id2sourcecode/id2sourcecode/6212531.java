    private void copyDependentWarContents(File srcDir, File targetDir) throws IOException, MojoExecutionException {
        DirectoryScanner scanner = new DirectoryScanner();
        scanner.setBasedir(srcDir);
        scanner.setExcludes(getDependentWarExcludes());
        scanner.addDefaultExcludes();
        scanner.setIncludes(getDependentWarIncludes());
        scanner.scan();
        String[] dirs = scanner.getIncludedDirectories();
        for (int j = 0; j < dirs.length; j++) {
            new File(targetDir, dirs[j]).mkdirs();
        }
        String[] files = scanner.getIncludedFiles();
        for (int j = 0; j < files.length; j++) {
            File targetFile = new File(targetDir, files[j]);
            if (!targetFile.exists()) {
                try {
                    targetFile.getParentFile().mkdirs();
                    FileUtils.copyFile(new File(srcDir, files[j]), targetFile);
                } catch (IOException e) {
                    throw new MojoExecutionException("Error copying file '" + files[j] + "' to '" + targetFile + "'", e);
                }
            }
        }
    }
