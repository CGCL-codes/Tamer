    public boolean migrateRepositoriesData(IProgressMonitor monitor) {
        File oldRepositoriesFile = new File(dataDirectory, TaskRepositoryManager.OLD_REPOSITORIES_FILE);
        File newRepositoriesFile = new File(dataDirectory, TaskRepositoryManager.DEFAULT_REPOSITORIES_FILE);
        if (!oldRepositoriesFile.exists()) return false;
        if (newRepositoriesFile.exists()) {
            if (!newRepositoriesFile.delete()) {
                MylarStatusHandler.fail(null, "Could not overwrite repositories file. Check read/write permission on data directory.", false);
                return false;
            }
        }
        ArrayList<File> filesToZip = new ArrayList<File>();
        filesToZip.add(oldRepositoriesFile);
        try {
            monitor.beginTask("Migrate Repository Data", 1);
            ZipFileUtil.createZipFile(newRepositoriesFile, filesToZip, new SubProgressMonitor(monitor, 1));
            if (!oldRepositoriesFile.delete()) {
                MylarStatusHandler.fail(null, "Could not remove old repositories file. Check read/write permission on data directory.", false);
                return false;
            }
            monitor.worked(1);
        } catch (Exception e) {
            MylarStatusHandler.fail(e, "Error occurred while migrating old repositories data: " + e.getMessage(), true);
            return false;
        } finally {
            monitor.done();
        }
        return true;
    }
