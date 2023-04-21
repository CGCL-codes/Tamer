    private void process(File srcDir, String destDirname) throws MojoExecutionException {
        File destDir = new File(destDirname);
        DirectoryScanner scanner = new DirectoryScanner();
        if (includes != null) {
            scanner.setIncludes(includes);
        } else {
            scanner.setIncludes(DEFAULT_INCLUDES);
        }
        scanner.setBasedir(srcDir);
        scanner.setCaseSensitive(true);
        scanner.scan();
        String[] files = scanner.getIncludedFiles();
        for (String file : files) {
            File srcFile = new File(srcDir, file);
            File destFile = new File(destDir, file);
            if (mergeSuffix != null) {
                String name = destFile.getName();
                int ind = name.indexOf('.');
                if (ind > -1) {
                    name = name.substring(0, ind) + mergeSuffix + name.substring(ind);
                } else {
                    throw new MojoExecutionException("Expected file name containing file extension: " + destFile.getAbsolutePath());
                }
                destFile = new File(destFile.getParentFile(), name);
            }
            if (!destFile.exists()) {
                try {
                    if (!destFile.getParentFile().exists()) {
                        destFile.getParentFile().mkdirs();
                    }
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException x) {
                    throw new MojoExecutionException("Error copying statusmessages from '" + srcFile.getAbsolutePath() + "' to '" + destFile.getAbsolutePath() + "'.", x);
                }
                try {
                    Document doc = Xml.parseMutable(destFile);
                    addComment(doc, file);
                    Xml.serialize(doc, destFile, true, true);
                } catch (Exception x) {
                    throw new MojoExecutionException("Error adding comment to statusmessages file '" + destFile.getAbsolutePath() + "'.", x);
                }
                getLog().info("Created " + destFile + " from source file " + srcFile);
            } else {
                try {
                    InputSource src = new InputSource();
                    src.setSystemId(srcFile.toURI().toString());
                    src.setByteStream(new FileInputStream(srcFile));
                    Merge merge = new Merge(src, selection, destFile, false);
                    merge.run();
                } catch (Exception x) {
                    throw new MojoExecutionException("Merging to file " + destFile.getAbsolutePath() + " failed.", x);
                }
                getLog().info("Merged source file " + srcFile + " into " + destFile);
            }
        }
    }
