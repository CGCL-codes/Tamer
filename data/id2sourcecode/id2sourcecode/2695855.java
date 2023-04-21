    public boolean migrateActivityData(IProgressMonitor monitor) {
        File oldActivityFile = new File(dataDirectory, MylarContextManager.OLD_CONTEXT_HISTORY_FILE_NAME + MylarContextManager.CONTEXT_FILE_EXTENSION_OLD);
        if (!oldActivityFile.exists()) return false;
        File contextsFolder = new File(dataDirectory, MylarContextManager.CONTEXTS_DIRECTORY);
        if (!contextsFolder.exists()) {
            if (!contextsFolder.mkdir()) {
                MylarStatusHandler.fail(null, "Could not create contexts folder. Check read/write permission on data directory.", false);
                return false;
            }
        }
        File newActivityFile = new File(contextsFolder, MylarContextManager.CONTEXT_HISTORY_FILE_NAME + MylarContextManager.CONTEXT_FILE_EXTENSION);
        if (newActivityFile.exists()) {
            if (!newActivityFile.delete()) {
                MylarStatusHandler.fail(null, "Could not overwrite activity file. Check read/write permission on data directory.", false);
                return false;
            }
        }
        ArrayList<File> filesToZip = new ArrayList<File>();
        filesToZip.add(oldActivityFile);
        try {
            monitor.beginTask("Migrate Activity Data", 1);
            ZipFileUtil.createZipFile(newActivityFile, filesToZip, new SubProgressMonitor(monitor, 1));
            if (!oldActivityFile.delete()) {
                MylarStatusHandler.fail(null, "Could not remove old activity file. Check read/write permission on data directory.", false);
                return false;
            }
            monitor.worked(1);
        } catch (Exception e) {
            MylarStatusHandler.fail(e, "Error occurred while migrating old activity data: " + e.getMessage(), true);
            return false;
        } finally {
            monitor.done();
        }
        return true;
    }
