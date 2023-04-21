    @Override
    public void run() {
        String fileExt = ConcernTagger.getResourceString("actions.SaveMetricsAction.FileExt");
        String path = "";
        boolean done = false;
        while (!done) {
            final FileDialog fileSaveDialog = new FileDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), SWT.SAVE);
            fileSaveDialog.setText(ConcernTagger.getResourceString("actions.SaveMetricsAction.DialogTitle"));
            fileSaveDialog.setFilterNames(new String[] { ConcernTagger.getResourceString("actions.SaveMetricsAction.DialogFilterName"), "All Files (*.*)" });
            fileSaveDialog.setFilterExtensions(new String[] { "*" + fileExt, "*.*" });
            String suggested = suggestedPrefix;
            if (!suggested.isEmpty()) suggested += ".";
            suggested += "metrics" + fileExt;
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
        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(path);
            PrintStream out = new PrintStream(stream);
            metricsTable.output(out);
        } catch (IOException e) {
            ProblemManager.reportException(e);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    ProblemManager.reportException(e);
                }
            }
        }
    }
