    private void copyApplicationStub() throws BuildException {
        if (mVerbose) {
            System.out.println("Copying JavaApplicationStub...");
        }
        File newStubFile = new File(mMacOsDir, "JavaApplicationStub");
        try {
            mFileUtils.copyFile(mStubFile, newStubFile);
        } catch (IOException ex) {
            throw new BuildException("Cannot copy JavaApplicationStub: " + ex);
        }
        try {
            setExecutable(newStubFile);
        } catch (IOException ex) {
            throw new BuildException("Cannot set executable bit: " + ex);
        }
    }
