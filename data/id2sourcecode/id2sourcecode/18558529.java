    private void report(IProject p) {
        File destinationDir = getReportingDirectory(p);
        FileFinder finder = new FileFinder();
        File dataFile = new File(p.getLocation().toString() + File.separator + ClassInstrumentor.DATA_FILE);
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            CoberclipseLog.error(e);
        }
        ProjectData projectData = CoverageDataFileHandler.loadCoverageData(dataFile);
        Collection<IPath> srcPaths = getSourcePaths(p, finder);
        ComplexityCalculator complexity = new ComplexityCalculator(finder);
        CoverageCollector cc = new CoverageCollector(projectData, complexity, p, srcPaths);
        final Collection<ICoverageItem> items = cc.getResult();
        PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {

            public void run() {
                CoverageManager.getInstance().updateCoverageItems(items);
            }
        });
        try {
            new HTMLReport(projectData, destinationDir, finder, complexity);
            if (!p.getFolder(ClassInstrumentor.REPORTING_PATH).exists()) {
                p.refreshLocal(IProject.DEPTH_ONE, null);
            }
            FileUtils.copyFile(new File(p.getLocation().toString() + File.separator + ClassInstrumentor.ORIGINAL_DATA_FILE), dataFile);
        } catch (IOException e) {
            CoberclipseLog.error(e);
        } catch (Exception e) {
            CoberclipseLog.error(e);
        }
    }
