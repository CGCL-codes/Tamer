    private void processExecFileSets() {
        for (Iterator execIter = mExecFileSets.iterator(); execIter.hasNext(); ) {
            FileSet fs = (FileSet) execIter.next();
            Project p = fs.getProject();
            File srcDir = fs.getDir(p);
            FileScanner ds = fs.getDirectoryScanner(p);
            fs.setupDirectoryScanner(ds, p);
            ds.scan();
            String[] files = ds.getIncludedFiles();
            try {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i];
                    File src = new File(srcDir, fileName);
                    File dest = new File(mMacOsDir, fileName);
                    if (mVerbose) {
                        System.out.println("Copying from " + src + " to " + dest);
                    }
                    mFileUtils.copyFile(src, dest);
                    setExecutable(dest);
                }
            } catch (IOException ex) {
                throw new BuildException("Cannot copy exec file: " + ex);
            }
        }
    }
