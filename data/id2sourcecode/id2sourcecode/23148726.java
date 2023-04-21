    protected IProject[] build(int kind, Map args, IProgressMonitor monitor) throws CoreException {
        IProject p = getProject();
        File dataFile = new File(p.getLocation().toString() + File.separator + ORIGINAL_DATA_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
        }
        IFolder instrumentedDir = p.getFolder(INSTRUMENTED_PATH);
        if (instrumentedDir == null || !instrumentedDir.exists()) {
            instrumentedDir.create(true, true, null);
        }
        System.setProperty("net.sourceforge.cobertura.datafile", dataFile.getAbsolutePath());
        System.setProperty("net.sourceforge.cobertura.instrumentedDir", instrumentedDir.getLocation().toString());
        if (kind == FULL_BUILD) {
            fullBuild();
        } else if (kind == AUTO_BUILD) {
            fullBuild();
        } else {
            IResourceDelta delta = getDelta(getProject());
            if (delta == null) fullBuild(); else incrementalBuild(delta);
        }
        File destdataFile = new File(p.getLocation().toString() + File.separator + DATA_FILE);
        try {
            FileUtils.copyFile(dataFile, destdataFile);
        } catch (IOException e) {
            CoberclipseLog.error(e);
        }
        return null;
    }
