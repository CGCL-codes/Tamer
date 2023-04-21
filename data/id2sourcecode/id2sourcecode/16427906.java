    @Override
    public boolean performTaskDone(final RootContext rootCtx, final RunContext runCtx) {
        boolean ok = true;
        if (log.isInfoEnabled()) log.info("SeSAM.performTaskDone : " + rootCtx);
        if (MAIN_TASK.equals(runCtx.getName())) {
            if (runCtx.getState() == RunState.STATE_FINISHED_OK) {
                preparePlotTask(runCtx.getParent());
                ok = true;
            } else {
                ok = false;
            }
        } else if (PLOT_TASK.equals(runCtx.getName())) {
            File dir = new File(runCtx.getWorkingDir());
            if (readme != null) {
                try {
                    FileUtils.copyFile(readme, new File(dir.getAbsolutePath() + "/README.txt"));
                } catch (IOException e) {
                    if (log.isWarnEnabled()) log.warn("Failed copying README.txt file to working directory.");
                }
            }
            File[] filesToCompress = dir.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    String name = file.getName();
                    return (name.startsWith("catalog") || PARAMETER_FILE.equals(name) || name.equalsIgnoreCase("README.txt"));
                }
            });
            final File zipFile = new File(dir.getAbsolutePath() + "/result.zip");
            FileUtils.compress(filesToCompress, zipFile);
            File[] filesToDelete = dir.listFiles(new FileFilter() {

                public boolean accept(File file) {
                    String name = file.getName();
                    return !(name.endsWith(".png") || file.equals(zipFile) || PARAMETER_FILE.equals(name));
                }
            });
            for (File file : filesToDelete) {
                file.delete();
            }
        }
        return ok;
    }
