    public void createArchive(String name) {
        ProjectLayout layout = getParent().getLayout();
        currentEntry = getEntry(name).getArchiveEntry();
        currentAssembleTask = currentEntry.createAssembleTask(layout.getClassesDir());
        ArgRunnable<ClassesArchive> config = currentEntry.getConfig();
        if (config != null) {
            config.run(this);
        }
        currentAssembleTask.getJar().execute();
        boolean isClasspathLib = getEntry(name).isClasspathLib();
        if (isClasspathLib) {
            FileUtils.copyFileToDirectory(currentEntry.getArtifact(), layout.getLibDir());
        }
    }
