    public boolean serializeProjectCache(final Project prj, boolean showDialogs) {
        if (prj != null && prj.getCachePath() != null) {
            final String cachePath = (String) prj.getCachePath();
            Assert.must(cachePath != null);
            if (RefactorItConstants.debugInfo) {
                log.debug("serializing project to " + cachePath);
            }
            Runnable writerTask = new Runnable() {

                public void run() {
                    ASTTreeCache.writeCache(prj.getProjectLoader().getAstTreeCache(), cachePath);
                }
            };
            if (showDialogs) {
                try {
                    JProgressDialog.run(createProjectContext(), writerTask, "Serializing cache", false);
                } catch (SearchingInterruptedException ex) {
                    if (Assert.enabled) {
                        Assert.must(false, "SearchingInterruptedException caught");
                    }
                    return false;
                }
            } else {
                Thread writingThread = new Thread(writerTask);
                writingThread.start();
                try {
                    writingThread.join();
                } catch (InterruptedException ex1) {
                    AppRegistry.getExceptionLogger().error(ex1, this);
                }
            }
        }
        this.projectsCache.clear();
        return true;
    }
