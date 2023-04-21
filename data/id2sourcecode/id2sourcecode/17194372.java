    public void run(List<AbstractRepositoryQuery> queries) {
        Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        FileDialog dialog = new FileDialog(shell, SWT.PRIMARY_MODAL | SWT.SAVE);
        dialog.setFilterExtensions(new String[] { "*" + ITasksUiConstants.FILE_EXTENSION });
        if (queries.size() == 1) {
            dialog.setFileName(queries.get(0).getHandleIdentifier());
        } else {
            String fomratString = "yyyy-MM-dd";
            SimpleDateFormat format = new SimpleDateFormat(fomratString, Locale.ENGLISH);
            String date = format.format(new Date());
            dialog.setFileName(date + "-exported-queries");
        }
        String path = dialog.open();
        if (path != null) {
            File file = new File(path);
            if (file.isDirectory()) {
                MessageDialog.openError(shell, "Query Export Error", "Could not export query because specified location is a folder");
                return;
            }
            if (file.exists()) {
                if (!MessageDialog.openConfirm(shell, "Confirm File Replace", "The file " + file.getPath() + " already exists. Do you want to overwrite it?")) {
                    return;
                }
            }
            TasksUiPlugin.getTaskListManager().getTaskListWriter().writeQueries(queries, file);
        }
        return;
    }
