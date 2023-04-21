    private void processJarAttrs() throws BuildException {
        try {
            for (Iterator jarIter = mJarAttrs.iterator(); jarIter.hasNext(); ) {
                File src = (File) jarIter.next();
                File dest = new File(mJavaDir, src.getName());
                if (mVerbose) {
                    System.out.println("Copying from " + src + " to " + dest);
                }
                mFileUtils.copyFile(src, dest);
                addToClasspath(dest.getName());
            }
        } catch (IOException ex) {
            throw new BuildException("Cannot copy jar file: " + ex);
        }
    }
