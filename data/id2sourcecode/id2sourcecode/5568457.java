    @Override
    public void run() {
        String arffFileExt = ConcernTagger.getResourceString("actions.ExportConcernsAction.FileExt1");
        String txtFileExt = ConcernTagger.getResourceString("actions.ExportConcernsAction.FileExt2");
        String path = "";
        boolean done = false;
        while (!done) {
            final FileDialog fileSaveDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
            fileSaveDialog.setText(ConcernTagger.getResourceString("actions.ExportConcernsAction.DialogTitle"));
            fileSaveDialog.setFilterNames(new String[] { ConcernTagger.getResourceString("actions.ExportConcernsAction.DialogFilterName1"), ConcernTagger.getResourceString("actions.ExportConcernsAction.DialogFilterName2") });
            fileSaveDialog.setFilterExtensions(new String[] { "*" + arffFileExt, "*" + txtFileExt });
            String suggested = suggestedPrefix;
            if (suggested.isEmpty()) suggested = "concerns";
            suggested += arffFileExt;
            fileSaveDialog.setFileName(suggested);
            path = fileSaveDialog.open();
            if (path == null || path.isEmpty()) return;
            if (path.lastIndexOf('.') == -1) {
                path += arffFileExt;
            }
            if (new File(path).exists()) {
                done = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Confirm File Overwrite", "The file already exists. Overwrite?");
            } else {
                done = true;
            }
        }
        int lastDot = path.lastIndexOf('.');
        if (lastDot > -1) {
            String fileExt = path.substring(lastDot);
            outputARFF = fileExt.compareToIgnoreCase(arffFileExt) == 0;
        } else {
            outputARFF = true;
        }
        final String pathForJob = path;
        Job job = new Job("Exporting concerns...") {

            @Override
            protected IStatus run(IProgressMonitor progressMonitor) {
                return saveConcernsToFile(pathForJob, progressMonitor, statusLineManager);
            }
        };
        job.setUser(true);
        job.schedule();
    }
