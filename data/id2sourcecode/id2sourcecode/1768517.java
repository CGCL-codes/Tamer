    @Override
    public boolean performFinish() {
        TasksUiPlugin.getTaskListManager().deactivateTask(TasksUiPlugin.getTaskListManager().getTaskList().getActiveTask());
        File sourceDirFile = null;
        File sourceZipFile = null;
        File sourceTaskListFile = null;
        File sourceRepositoriesFile = null;
        File sourceActivationHistoryFile = null;
        List<File> contextFiles = new ArrayList<File>();
        List<ZipEntry> zipFilesToExtract = new ArrayList<ZipEntry>();
        boolean overwrite = importPage.overwrite();
        String sourceZip = importPage.getSourceZipFile();
        sourceZipFile = new File(sourceZip);
        if (!sourceZipFile.exists()) {
            MessageDialog.openError(getShell(), "File not found", sourceZipFile.toString() + " could not be found.");
            return false;
        }
        Enumeration<? extends ZipEntry> entries;
        ZipFile zipFile;
        try {
            zipFile = new ZipFile(sourceZipFile, ZipFile.OPEN_READ);
            entries = zipFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.isDirectory()) {
                    continue;
                }
                if (!importPage.importTaskList() && entry.getName().endsWith(ITasksUiConstants.OLD_TASK_LIST_FILE)) {
                    continue;
                }
                if (!importPage.importActivationHistory() && entry.getName().endsWith(InteractionContextManager.CONTEXT_HISTORY_FILE_NAME + InteractionContextManager.CONTEXT_FILE_EXTENSION_OLD)) {
                    continue;
                }
                if (!importPage.importTaskContexts() && entry.getName().matches(".*-\\d*" + InteractionContextManager.CONTEXT_FILE_EXTENSION_OLD + "$")) {
                    continue;
                }
                File destContextFile = new File(TasksUiPlugin.getDefault().getDataDirectory() + File.separator + entry.getName());
                if (!overwrite && destContextFile.exists()) {
                    if (MessageDialog.openConfirm(getShell(), "File exists!", "Overwrite existing file?\n" + destContextFile.getName())) {
                        zipFilesToExtract.add(entry);
                    } else {
                    }
                } else {
                    zipFilesToExtract.add(entry);
                }
            }
        } catch (IOException e) {
            StatusHandler.fail(e, "Could not import files", true);
        }
        FileCopyJob job = new FileCopyJob(sourceDirFile, sourceZipFile, sourceTaskListFile, sourceRepositoriesFile, sourceActivationHistoryFile, contextFiles, zipFilesToExtract);
        IProgressService service = PlatformUI.getWorkbench().getProgressService();
        try {
            service.run(true, false, job);
        } catch (InvocationTargetException e) {
            StatusHandler.fail(e, "Could not import files", true);
        } catch (InterruptedException e) {
            StatusHandler.fail(e, "Could not import files", true);
        }
        importPage.saveSettings();
        return true;
    }
