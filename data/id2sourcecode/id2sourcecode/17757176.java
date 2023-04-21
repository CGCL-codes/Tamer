    private void processExecFileLists() throws BuildException {
        for (Iterator execIter = mExecFileLists.iterator(); execIter.hasNext(); ) {
            FileList fl = (FileList) execIter.next();
            Project p = fl.getProject();
            File srcDir = fl.getDir(p);
            String[] files = fl.getFiles(p);
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
                throw new BuildException("Cannot copy jar file: " + ex);
            }
        }
    }
