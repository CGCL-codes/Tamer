    private void moveOutput(IProject project, IContainer sourceDir, IProgressMonitor monitor) throws CoreException {
        IFolder outputDir = TexlipseProperties.getProjectOutputDir(project);
        IPath sourceDirPath = sourceDir.getProjectRelativePath();
        IPath outputDirPath = null;
        if (outputDir != null) {
            outputDirPath = outputDir.getProjectRelativePath();
        } else {
            outputDirPath = project.getProjectRelativePath();
        }
        if (sourceDirPath.equals(outputDirPath)) {
            return;
        }
        String outputFileName = TexlipseProperties.getOutputFileName(project);
        IResource outputFile = sourceDir.findMember(outputFileName);
        if (outputFile != null && outputFile.exists()) {
            if (outputDir != null && !outputDir.exists()) {
                outputDir.create(true, true, null);
            }
            IResource dest = null;
            if (outputDir != null) {
                dest = outputDir.getFile(outputFileName);
            } else {
                dest = project.getFile(outputFileName);
            }
            if (dest == null) return;
            if (dest.exists()) {
                File outFile = new File(outputFile.getLocationURI());
                File destFile = new File(dest.getLocationURI());
                try {
                    FileOutputStream out = new FileOutputStream(destFile);
                    out.getChannel().tryLock();
                    BufferedInputStream in = new BufferedInputStream(new FileInputStream(outFile));
                    byte[] buf = new byte[4096];
                    int l;
                    while ((l = in.read(buf)) != -1) {
                        out.write(buf, 0, l);
                    }
                    in.close();
                    out.close();
                    outputFile.delete(true, monitor);
                } catch (IOException e) {
                    dest.delete(true, monitor);
                    outputFile.move(dest.getFullPath(), true, monitor);
                }
            } else {
                outputFile.move(dest.getFullPath(), true, monitor);
            }
            monitor.worked(1);
            sourceDir.refreshLocal(IProject.DEPTH_INFINITE, monitor);
            monitor.worked(1);
            if (outputDir != null) {
                outputDir.refreshLocal(IProject.DEPTH_ONE, monitor);
            } else {
                project.refreshLocal(IProject.DEPTH_ONE, monitor);
            }
            monitor.worked(1);
        }
    }
