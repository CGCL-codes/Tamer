    @Override
    public void execute() throws BuildException {
        int createNo = 0;
        int mergeNo = 0;
        if (srcDir.exists()) {
            DirectoryScanner scanner = getDirectoryScanner(srcDir);
            String[] files = scanner.getIncludedFiles();
            for (String file : files) {
                File srcFile = new File(srcDir, file);
                File destFile = new File(destDir, file);
                if (suffix != null) {
                    String name = destFile.getName();
                    int ind = name.indexOf('.');
                    if (ind > -1) {
                        name = name.substring(0, ind) + suffix + name.substring(ind);
                    } else throw new BuildException("Expected file name containing file extension: " + destFile.getAbsolutePath());
                    destFile = new File(destFile.getParentFile(), name);
                }
                boolean destExists = destFile.exists();
                if (!destExists) {
                    try {
                        if (!destFile.getParentFile().exists()) destFile.getParentFile().mkdirs();
                        FileUtils.copyFile(srcFile, destFile);
                    } catch (IOException x) {
                        throw new BuildException("Error copying statusmessages from '" + srcFile.getAbsolutePath() + "' to '" + destFile.getAbsolutePath() + "'.", x);
                    }
                    try {
                        Document doc = Xml.parseMutable(destFile);
                        addComment(doc, file);
                        Xml.serialize(doc, destFile, true, true);
                    } catch (Exception x) {
                        throw new BuildException("Error adding comment to statusmessages file '" + destFile.getAbsolutePath() + "'.", x);
                    }
                    createNo++;
                } else if (srcFile.lastModified() > destFile.lastModified()) {
                    try {
                        Merge merge = new Merge(srcFile, selection, destFile, false);
                        merge.run();
                    } catch (Exception x) {
                        throw new BuildException("Merging to file " + destFile.getAbsolutePath() + " failed.", x);
                    }
                    mergeNo++;
                }
            }
        }
        if (createNo > 0) log("Created " + createNo + " statusmessage merge file" + (createNo > 1 ? "s" : "") + ".");
        if (mergeNo > 0) log("Merged " + mergeNo + " statusmessage file" + (mergeNo > 1 ? "s" : "") + ".");
    }
