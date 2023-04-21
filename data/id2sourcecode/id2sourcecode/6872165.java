    @Override
    public boolean performFinish() {
        boolean overwrite = exportPage.overwrite();
        boolean zip = exportPage.zip();
        Collection<AbstractTask> taskContextsToExport = TasksUiPlugin.getTaskListManager().getTaskList().getAllTasks();
        String destDir = exportPage.getDestinationDirectory();
        final File destDirFile = new File(destDir);
        if (!destDirFile.exists() || !destDirFile.isDirectory()) {
            StatusHandler.fail(new Exception("File Export Exception"), "Could not export data because specified location does not exist or is not a folder", true);
            return false;
        }
        final File destTaskListFile = new File(destDir + File.separator + ITasksUiConstants.DEFAULT_TASK_LIST_FILE);
        final File destActivationHistoryFile = new File(destDir + File.separator + InteractionContextManager.CONTEXT_HISTORY_FILE_NAME + InteractionContextManager.CONTEXT_FILE_EXTENSION);
        final File destZipFile = new File(destDir + File.separator + getZipFileName());
        if (!overwrite) {
            if (zip) {
                if (destZipFile.exists()) {
                    if (!MessageDialog.openConfirm(getShell(), "Confirm File Replace", "The zip file " + destZipFile.getPath() + " already exists. Do you want to overwrite it?")) {
                        return false;
                    }
                }
            } else {
                if (exportPage.exportTaskList() && destTaskListFile.exists()) {
                    if (!MessageDialog.openConfirm(getShell(), "Confirm File Replace", "The task list file " + destTaskListFile.getPath() + " already exists. Do you want to overwrite it?")) {
                        return false;
                    }
                }
                if (exportPage.exportActivationHistory() && destActivationHistoryFile.exists()) {
                    if (!MessageDialog.openConfirm(getShell(), "Confirm File Replace", "The task activation history file " + destActivationHistoryFile.getPath() + " already exists. Do you want to overwrite it?")) {
                        return false;
                    }
                }
                if (exportPage.exportTaskContexts()) {
                    for (AbstractTask task : taskContextsToExport) {
                        File contextFile = ContextCorePlugin.getContextManager().getFileForContext(task.getHandleIdentifier());
                        File destTaskFile = new File(destDir + File.separator + contextFile.getName());
                        if (destTaskFile.exists()) {
                            if (!MessageDialog.openConfirm(getShell(), "Confirm File Replace", "Task context files already exist in " + destDir + ". Do you want to overwrite them?")) {
                                return false;
                            } else {
                                break;
                            }
                        }
                    }
                }
            }
        }
        TaskDataExportJob job = new TaskDataExportJob(exportPage.getDestinationDirectory(), exportPage.exportTaskList(), exportPage.exportActivationHistory(), exportPage.exportTaskContexts(), exportPage.zip(), destZipFile.getName(), taskContextsToExport);
        IProgressService service = PlatformUI.getWorkbench().getProgressService();
        try {
            service.run(true, false, job);
        } catch (InvocationTargetException e) {
            StatusHandler.fail(e, "Could not export files", true);
        } catch (InterruptedException e) {
            StatusHandler.fail(e, "Could not export files", true);
        }
        exportPage.saveSettings();
        return true;
    }
