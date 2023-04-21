    @Override
    public void run() {
        String fileExt = ConcernTagger.getResourceString("actions.ExportLinksAction.FileExt");
        String path = "";
        boolean done = false;
        while (!done) {
            final FileDialog fileSaveDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
            fileSaveDialog.setText(ConcernTagger.getResourceString("actions.ExportLinksAction.DialogTitle"));
            fileSaveDialog.setFilterNames(new String[] { ConcernTagger.getResourceString("actions.ExportLinksAction.DialogFilterName"), "All Files (*.*)" });
            fileSaveDialog.setFilterExtensions(new String[] { "*" + fileExt, "*.*" });
            String suggested = suggestedPrefix;
            if (suggested.isEmpty()) suggested = "links";
            suggested += fileExt;
            fileSaveDialog.setFileName(suggested);
            path = fileSaveDialog.open();
            if (path == null || path.isEmpty()) return;
            if (path.indexOf('.') == -1) path += fileExt;
            if (new File(path).exists()) {
                done = MessageDialog.openQuestion(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), "Confirm File Overwrite", "The file already exists. Overwrite?");
            } else {
                done = true;
            }
        }
        final String pathForJob = path;
        Job job = new Job("Exporting links...") {

            @Override
            protected IStatus run(IProgressMonitor progressMonitor) {
                return saveLinkFile(pathForJob, progressMonitor, statusLineManager);
            }
        };
        job.setUser(true);
        job.schedule();
    }
