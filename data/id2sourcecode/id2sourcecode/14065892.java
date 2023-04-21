    public void fileClosed(ManagedFileWriter mfw) {
        if (checkFileWhenComplete) {
            File existing = new File(outFolder + "/" + mfw.fileName);
            File newgen = new File(outFolder + "/NEW" + mfw.fileName);
            try {
                if (!FileUtils.contentEquals(existing, newgen)) {
                    FileUtils.copyFile(newgen, existing);
                    filesReplaced++;
                    reportProgress("     Replacing: " + existing.getAbsolutePath());
                }
                if (!newgen.delete()) reportError("    Could not delete temporary file: " + newgen.getAbsolutePath());
            } catch (IOException e) {
                throw (new IllegalStateException("File comparison failed.", e));
            }
        }
        lastFolder = outFolder;
    }
