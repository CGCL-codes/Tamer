    public void export() {
        boolean mayHaveChildren = false;
        if (viewManager.getControl() instanceof Tree) {
            mayHaveChildren = true;
        }
        ExportRowsToFileDialog exportRowsToFileDialog = new ExportRowsToFileDialog(ViewUtil.getCurrentShell(), mayHaveChildren);
        if (exportRowsToFileDialog.open() == IDialogConstants.OK_ID) {
            exportFilename = exportRowsToFileDialog.getFilePath();
            includeHiddenChildren = exportRowsToFileDialog.includeHiddenChildren();
            File file = new File(exportFilename);
            if (file.exists()) {
                if (!DialogUtil.openQuestionDialog("Export Rows", "The export file already exists. Do you want to overwrite it?")) {
                    return;
                }
            }
            separator = exportRowsToFileDialog.getSeparator();
            IExportRows exportRows = null;
            if (!includeHiddenChildren) {
                if (viewManager.getControl() instanceof Tree) {
                    exportRows = new ExportRows(viewManager, columnManager, separator);
                } else {
                    exportRows = new ExportTableRows(viewManager, columnManager, separator);
                }
            } else {
                exportRows = new ExportWrappers(viewManager, columnManager, separator);
            }
            exportRows.export(exportFilename);
            if (exportRowsToFileDialog.openFile()) {
                ArtifactManager.open(file.getAbsolutePath());
            }
        }
    }
