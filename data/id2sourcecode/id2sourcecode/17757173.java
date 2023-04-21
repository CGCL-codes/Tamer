    private void processJarFileLists() throws BuildException {
        for (Iterator jarIter = mJarFileLists.iterator(); jarIter.hasNext(); ) {
            JarFileList fl = (JarFileList) jarIter.next();
            Project p = fl.getProject();
            File srcDir = fl.getDir(p);
            String[] files = fl.getFiles(p);
            try {
                for (int i = 0; i < files.length; i++) {
                    String fileName = files[i];
                    File src = new File(srcDir, fileName);
                    File dest = new File(mJavaDir, fileName);
                    if (mVerbose) {
                        System.out.println("Copying from " + src + " to " + dest);
                    }
                    mFileUtils.copyFile(src, dest);
                    if (fl.addAllToClasspath || fileName.endsWith(".jar")) addToClasspath(fileName);
                }
            } catch (IOException ex) {
                throw new BuildException("Cannot copy jar file: " + ex);
            }
        }
    }
