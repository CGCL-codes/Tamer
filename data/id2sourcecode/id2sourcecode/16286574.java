    public boolean migrateTaskContextData(IProgressMonitor monitor) {
        ArrayList<File> contextFiles = new ArrayList<File>();
        for (File file : dataDirectory.listFiles()) {
            if (file.getName().startsWith("http") || file.getName().startsWith("local") || file.getName().startsWith("task")) {
                if (!file.getName().endsWith(".zip")) {
                    contextFiles.add(file);
                }
            }
        }
        try {
            monitor.beginTask("Task Context Migration", contextFiles.size());
            File contextsFolder = new File(dataDirectory, WorkspaceAwareContextStore.CONTEXTS_DIRECTORY);
            if (!contextsFolder.exists()) {
                if (!contextsFolder.mkdir()) {
                    StatusHandler.fail(null, "Could not create contexts folder. Check read/write permission on data directory.", false);
                    return false;
                }
            }
            for (File file : contextFiles) {
                ArrayList<File> filesToZip = new ArrayList<File>();
                filesToZip.add(file);
                File newContextFile = new File(contextsFolder, file.getName() + ".zip");
                if (newContextFile.exists()) {
                    if (!newContextFile.delete()) {
                        StatusHandler.fail(null, "Could not overwrite context file. Check read/write permission on data directory.", false);
                        return false;
                    }
                }
                ZipFileUtil.createZipFile(newContextFile, filesToZip, new SubProgressMonitor(monitor, 1));
                if (!file.delete()) {
                    StatusHandler.fail(null, "Could not remove old context file. Check read/write permission on data directory.", false);
                    return false;
                }
                monitor.worked(1);
            }
        } catch (Exception e) {
            StatusHandler.fail(e, "Error occurred while migrating old repositories data: " + e.getMessage(), true);
            return false;
        } finally {
            monitor.done();
        }
        return true;
    }
